package org.safa.maintenanceservice.controller;

import org.safa.maintenanceservice.models.dto.ResponseBody;
import org.safa.maintenanceservice.models.dto.user.auth.AuthUserResponse;
import org.safa.maintenanceservice.models.dto.user.auth.login.LoginUserRequest;
import org.safa.maintenanceservice.models.dto.user.auth.register.RegisterUserRequest;
import org.safa.maintenanceservice.models.exceptions.AlreadyExistsException;
import org.safa.maintenanceservice.models.exceptions.BadRequestException;
import org.safa.maintenanceservice.models.exceptions.NotFoundException;
import org.safa.maintenanceservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<ResponseBody<AuthUserResponse>> login(@RequestBody LoginUserRequest  loginUserRequest) {
        try {
            var response = userService.loginUser(loginUserRequest);
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ResponseBody<>(HttpStatus.OK.value(), response));
        }

        catch (UsernameNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new  ResponseBody<>(HttpStatus.NOT_FOUND.value(), e.getMessage()));
        }
        catch (BadRequestException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new  ResponseBody<>(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ResponseBody<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseBody<AuthUserResponse>> register(@RequestBody RegisterUserRequest request) {
        try {
            AuthUserResponse response = userService.registerUser(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ResponseBody<>(HttpStatus.CREATED.value(), response));
        }catch (BadRequestException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new  ResponseBody<>(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
        }
        catch (AlreadyExistsException e){
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new  ResponseBody<>(HttpStatus.CONFLICT.value(), e.getMessage()));
        } catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new  ResponseBody<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
        }
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<ResponseBody<AuthUserResponse>> refreshToken(@RequestParam String refreshToken) {
        try {
            AuthUserResponse response = userService.refreshToken(refreshToken);
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ResponseBody<>(HttpStatus.OK.value(), response));
        }catch (BadRequestException | NullPointerException e){
            String message;
            if(e.getMessage()==null || e.getMessage().isEmpty()) message = "Not found"; else message = e.getMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ResponseBody<>(HttpStatus.BAD_REQUEST.value(), message));
        }catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ResponseBody<>(HttpStatus.NOT_FOUND.value(), e.getMessage()));
        }
    }
//this is temporary comment
    @DeleteMapping("/log-out")
    public ResponseEntity<ResponseBody<Boolean>> logout(@RequestParam String refreshToken) {
        try {
            var response = userService.logout(refreshToken);
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ResponseBody<>(HttpStatus.OK.value(), response));
        }catch (BadRequestException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ResponseBody<>(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
        }catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ResponseBody<>(HttpStatus.NOT_FOUND.value(), e.getMessage()));
        }catch (NullPointerException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ResponseBody<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
        }
    }
}