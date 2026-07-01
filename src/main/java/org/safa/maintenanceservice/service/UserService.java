package org.safa.maintenanceservice.service;

import org.safa.maintenanceservice.models.dto.user.auth.AuthUserResponse;
import org.safa.maintenanceservice.models.dto.user.auth.login.LoginUserRequest;
import org.safa.maintenanceservice.models.dto.user.auth.register.RegisterUserRequest;
import org.safa.maintenanceservice.models.entity.user.UserEntity;
import org.safa.maintenanceservice.models.exceptions.AlreadyExistsException;
import org.safa.maintenanceservice.models.exceptions.BadRequestException;
import org.safa.maintenanceservice.models.exceptions.NotFoundException;
import org.safa.maintenanceservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
    private TokenService tokenService;

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
            //here we are saving the refreshToken
            tokenService.saveRefreshToken(userId, response.refreshToken());
            tokenService.saveUserIdToken(response.refreshToken(), userId);
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
        if (request.role()==null) {
            throw new BadRequestException("Role is required");
        }


        UserEntity entity = userRepository.save(new UserEntity(
                request.fullName(), request.username(), bCryptPasswordEncoder.encode(request.password()), request.phoneNumber(), request.role().name(), request.location().latitude(), request.location().longitude()
        ));
        AuthUserResponse response = jwtService.generateToken(request.username());
        try {
            var userId = userRepository.findByUsername(entity.getUsername()).orElseThrow(() -> new NotFoundException("Username not found")).getId();
            //here we are saving the refreshToken
            tokenService.saveRefreshToken(userId, response.refreshToken());
            tokenService.saveUserIdToken(response.refreshToken(), userId);
            return response;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public AuthUserResponse refreshToken(String refreshToken) throws NullPointerException {
        long userId = tokenService.getUserId(refreshToken);
        String token = tokenService.getRefreshToken(userId);
        if (userId==0L){
            throw new NotFoundException("User not found");
        }
        if (token == null) {
            throw new BadRequestException("Invalid refresh token");
        } else if (!token.equals(refreshToken)) {
            throw new BadRequestException("Invalid refresh token");
        }
        tokenService.deleteUserId(userId);
        tokenService.deleteUserId(refreshToken);
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        return jwtService.generateToken(user.getUsername());
    }

    public boolean logout(String refreshToken) {
        long userId = tokenService.getUserId(refreshToken);
        String token = tokenService.getRefreshToken(userId);
        if (userId==0L){
            throw new NotFoundException("User not found");
        }
        if (token == null) {
            throw new BadRequestException("Invalid refresh token");
        } else if (!token.equals(refreshToken)) {
            throw new BadRequestException("Invalid refresh token");
        }
        tokenService.deleteUserId(userId);
        tokenService.deleteUserId(refreshToken);
        return true;
    }

    public Boolean deleteUser(long userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
            return true;
        }else {
            return false;
        }
    }
}