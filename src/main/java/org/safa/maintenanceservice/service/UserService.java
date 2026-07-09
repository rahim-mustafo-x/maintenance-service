package org.safa.maintenanceservice.service;

import org.safa.maintenanceservice.models.dto.user.auth.AuthUserResponse;
import org.safa.maintenanceservice.models.dto.user.auth.SendCodeRequest;
import org.safa.maintenanceservice.models.dto.user.auth.login.LoginUserRequest;
import org.safa.maintenanceservice.models.dto.user.auth.register.RegisterUserRequest;
import org.safa.maintenanceservice.models.entity.user.UserEntity;
import org.safa.maintenanceservice.models.entity.user.role.RoleEntity;
import org.safa.maintenanceservice.models.entity.user.session.SessionEntity;
import org.safa.maintenanceservice.models.exceptions.AlreadyExistsException;
import org.safa.maintenanceservice.models.exceptions.BadRequestException;
import org.safa.maintenanceservice.models.exceptions.NotFoundException;
import org.safa.maintenanceservice.repository.SessionRedisRepository;
import org.safa.maintenanceservice.repository.RoleRepository;
import org.safa.maintenanceservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.Duration;

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
    private CodeService codeService;

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

    public boolean sendCode(SendCodeRequest sendCodeRequest) {
        var refreshToken = sendCodeRequest.refreshToken();
        var userId = sendCodeRequest.userId();

        return false;
    }
}