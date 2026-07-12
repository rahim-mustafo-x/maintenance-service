package org.safa.maintenanceservice.service;

import jakarta.servlet.http.HttpServletRequest;
import org.safa.maintenanceservice.models.dto.user.auth.AuthUserResponse;
import org.safa.maintenanceservice.models.dto.user.auth.ChangePasswordRequest;
import org.safa.maintenanceservice.models.dto.user.auth.CodeRequest;
import org.safa.maintenanceservice.models.dto.user.auth.login.LoginUserRequest;
import org.safa.maintenanceservice.models.dto.user.auth.register.RegisterUserRequest;
import org.safa.maintenanceservice.models.entity.user.UserEntity;
import org.safa.maintenanceservice.models.entity.user.role.RoleEntity;
import org.safa.maintenanceservice.models.entity.user.session.SessionEntity;
import org.safa.maintenanceservice.models.exceptions.AlreadyExistsException;
import org.safa.maintenanceservice.models.exceptions.BadRequestException;
import org.safa.maintenanceservice.models.exceptions.ExpiredException;
import org.safa.maintenanceservice.models.exceptions.NotFoundException;
import org.safa.maintenanceservice.repository.SessionRedisRepository;
import org.safa.maintenanceservice.repository.RoleRepository;
import org.safa.maintenanceservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private SessionRedisRepository sessionRedisRepository;

    @Autowired
    private CodeRedisService codeRedisService;

    @Value("${TELEGRAM_BOT_URL}")
    private String botUrl;

    public AuthUserResponse loginUser(LoginUserRequest loginUserRequest) {
        //here we are putting username & password
        try {
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginUserRequest.username(), loginUserRequest.password()
            ));
            //here we are checking if it is valid or not
            if (!authenticate.isAuthenticated()) {
                throw new BadCredentialsException("Bad credentials");
            }
            AuthUserResponse response = jwtService.generateToken(loginUserRequest.username());
            var userId = userRepository.findByUsername(loginUserRequest.username()).orElseThrow(()->new NotFoundException("Username not found")).getId();
            var roleEntity = roleRepository.findByRoleAndUserId(loginUserRequest.role(),  userId);
            if (roleEntity.isEmpty()){
                var userEntity = userRepository.findByUsernameId(userId);
                if (userEntity.isEmpty()){
                    throw new NotFoundException("Username not found");
                }
                roleRepository.save(new  RoleEntity(loginUserRequest.role(), userEntity.get()));
            }
            //here we are saving the refreshToken
            sessionRedisRepository.save(
                    new SessionEntity(userId, response.refreshToken(), Duration.ofDays(30).toSeconds())
            );
            return response;
        }catch (BadCredentialsException | UsernameNotFoundException e) {
            throw new BadRequestException("Bad credentials");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public AuthUserResponse registerUser(RegisterUserRequest request) {
        if (request.fullName().isEmpty()) {
            throw new BadRequestException("Full name is required");
        }else if (request.fullName().length() < 3) {
            throw new BadRequestException("Full name should be at least 3 characters");
        }

        if (request.password().isEmpty()) {
            throw new BadRequestException("Password is required");
        }
        else if (request.password().length() < 6) {
            throw new BadRequestException("Password should be at least 6 characters");
        }
        if (request.username().isEmpty()) {
            throw new BadRequestException("Username is required");
        }
        else if (request.username().length() < 8) {
            throw new BadRequestException("Username should be at least 8 characters");
        }
        else if (userRepository.existsByUsername(request.username())) {
            throw new AlreadyExistsException("Username already exists");
        }

        if (!request.phoneNumber().matches("^\\+?[0-9]{7,15}$")){
            throw new BadRequestException("Invalid phone number");
        } else if (userRepository.existsByPhoneNumber(request.phoneNumber())) {
            throw new AlreadyExistsException("Phone number already exists");
        } else if (request.phoneNumber().length() != 13) {
            throw new BadRequestException("The phone number should be 13 characters for Uzbekistan");
        }
        UserEntity entity = userRepository.save(new UserEntity(
                request.fullName(), request.username(), bCryptPasswordEncoder.encode(request.password()), request.phoneNumber()
        ));
        var role = roleRepository.save(new RoleEntity(request.role(), entity));
        IO.println(role);
        AuthUserResponse response = jwtService.generateToken(request.username());
        try {
            var userId = userRepository.findByUsername(entity.getUsername()).orElseThrow(() -> new NotFoundException("Username not found")).getId();
            //here we are saving the refreshToken
            sessionRedisRepository.save(
                    new SessionEntity(userId, response.refreshToken(), Duration.ofDays(30).toSeconds())
            );
            return response;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public AuthUserResponse refreshToken(String refreshToken) throws NullPointerException {
        var sessionEntity = sessionRedisRepository.findByRefreshToken(refreshToken);
        var userId = sessionEntity.getUserId();
        if (userId==0L){
            throw new NotFoundException("User not found");
        }
        if (sessionEntity.getRefreshToken() == null) {
            throw new BadRequestException("Invalid refresh token");
        } else if (!sessionEntity.getRefreshToken().equals(refreshToken)) {
            throw new BadRequestException("Invalid refresh token");
        }
        sessionRedisRepository.delete(userId);
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        return jwtService.generateToken(user.getUsername());
    }

    public boolean logout(String refreshToken) throws NullPointerException{
        SessionEntity sessionEntity = sessionRedisRepository.findByRefreshToken(refreshToken);
        var userId = sessionEntity.getUserId();
        if (userId==0L){
            throw new NotFoundException("User not found");
        }
        if (refreshToken == null) {
            throw new BadRequestException("Invalid refresh token");
        } else if (!sessionEntity.getRefreshToken().equals(refreshToken)) {
            throw new BadRequestException("Invalid refresh token");
        }
        sessionRedisRepository.deleteByRefreshToken(refreshToken);
        return true;
    }

    public Boolean deleteUser(long userId) {
        if (!userRepository.existsById(userId)){
            return false;
        }
        userRepository.deleteById(userId);
        return true;
    }

    public boolean sendCode(HttpServletRequest request) {
        var token = request.getHeader(HttpHeaders.AUTHORIZATION).substring(7).trim();
        long userId = jwtService.extractUserId(token);
        Optional<UserEntity> userEntity = userRepository.findByUsernameId(userId);
        if (userEntity.isPresent()) {
            var user = userEntity.get();
            var code = codeRedisService.generateCode(6);
            var codeRequest = new CodeRequest(code, user.getPhoneNumber());
            var objectMapper = new ObjectMapper();
            var rawBody = objectMapper.writeValueAsString(codeRequest);
            var client = RestClient.builder()
                    .baseUrl(botUrl)
                    .build();
            var response = client.post()
                    .uri("/send_code")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(rawBody)
                    .retrieve()
                    .body(new ParameterizedTypeReference<Map<String, String>>(){});
            var message = Objects.requireNonNull(response).get("message");
            if (message!=null){
                codeRedisService.saveCodeFor2Minutes(userId, user.getPhoneNumber(), code);
                return true;
            }else {
                return false;
            }
        }else {
            return false;
        }
    }

    public boolean changePassword(ChangePasswordRequest changePasswordRequest, HttpServletRequest request) {
        var token = request.getHeader(HttpHeaders.AUTHORIZATION).substring(7).trim();
        long userId = jwtService.extractUserId(token);
        Optional<UserEntity> userEntity = userRepository.findByUsernameId(userId);
        if (userEntity.isPresent()) {
            var user = userEntity.get();
            var code = codeRedisService.getCode(userId, user.getPhoneNumber());
            if (code==null){
                throw new NotFoundException("Code not found");
            }
            if (codeRedisService.isExpired(userId, user.getPhoneNumber())){
                throw new ExpiredException("Already expired");
            }
            if (!code.equals(changePasswordRequest.code())){
                throw new BadRequestException("Invalid code");
            }
            if (changePasswordRequest.newPassword()==null){
                throw new BadRequestException("New password is null");
            }
            if (changePasswordRequest.newPassword().isEmpty() || changePasswordRequest.newPassword().isBlank()){
                throw new BadRequestException("New password is empty");
            }
            if (changePasswordRequest.newPassword().length() < 6){
                throw new BadRequestException("Password too short");
            }
            userRepository.changePassword(userId, Objects.requireNonNull(bCryptPasswordEncoder.encode(changePasswordRequest.newPassword())));
            codeRedisService.deleteCode(userId, user.getPhoneNumber());
            return true;
        }else {
            return false;
        }
    }
}