package org.safa.maintenanceservice.service;

import org.safa.maintenanceservice.models.dto.user.auth.AuthUserResponse;
import org.safa.maintenanceservice.models.dto.user.auth.login.LoginUserRequest;
import org.safa.maintenanceservice.models.dto.user.auth.register.RegisterUserRequest;
import org.safa.maintenanceservice.models.entity.user.UserEntity;
import org.safa.maintenanceservice.models.exceptions.AlreadyExistsException;
import org.safa.maintenanceservice.models.exceptions.BadRequestException;
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
            return jwtService.generateToken(loginUserRequest.username());
        }catch (BadCredentialsException | UsernameNotFoundException e) {
            throw new BadRequestException("Bad credentials");
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
        if (request.userName().isEmpty()) {
            throw new BadRequestException("Username is required");
        }
        else if (request.userName().length() < 8) {
            throw new BadRequestException("Username should be at least 8 characters");
        }
        else if (userRepository.existsByUsername(request.userName())) {
            throw new AlreadyExistsException("Username already exists");
        }

        if (!request.phoneNumber().matches("^\\+?[0-9]{7,15}$")){
            throw new BadRequestException("Invalid phone number");
        } else if (userRepository.existsByPhoneNumber(request.phoneNumber())) {
            throw new AlreadyExistsException("Phone number already exists");
        }
        if (request.role()==null) {
            throw new BadRequestException("Role is required");
        }


        UserEntity entity = userRepository.save(new UserEntity(
                request.fullName(), request.userName(), bCryptPasswordEncoder.encode(request.password()), request.phoneNumber(), request.role().name(), request.location().latitude(), request.location().longitude()
        ));
        return jwtService.generateToken(entity.getUsername());
    }
}