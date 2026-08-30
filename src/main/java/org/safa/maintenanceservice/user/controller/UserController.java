package org.safa.maintenanceservice.user.controller;

import org.safa.maintenanceservice.ApiResponse;
import org.safa.maintenanceservice.user.model.dto.UpdateUserRequest;
import org.safa.maintenanceservice.user.model.dto.UserResponse;
import org.safa.maintenanceservice.admin.exceptions.AlreadyExistsException;
import org.safa.maintenanceservice.admin.exceptions.BadRequestException;
import org.safa.maintenanceservice.admin.exceptions.NotFoundException;
import org.safa.maintenanceservice.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/v1/user")
public class UserController {
    @Autowired
    private UserService userService;


    private long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = Objects.requireNonNull(authentication).getName();
        return userService.findUserIdByUserName(username);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<?>> deleteUser() {
        try {
            if (userService.deleteUser(getCurrentUserId())){
                return ResponseEntity.status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(ApiResponse.builder()
                                .code((HttpStatus.OK.value()))
                                .data(true)
                                .build());
            }else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(ApiResponse.builder()
                                .code(HttpStatus.NOT_FOUND.value())
                                .message("User has already been deleted")
                                .build());
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(ApiResponse.builder()
                            .code(HttpStatus.BAD_REQUEST.value())
                            .message(e.getMessage())
                            .build());
        }
    }


    @PutMapping("update")
    public ResponseEntity<ApiResponse<?>> updateUser(@RequestBody UpdateUserRequest request){
        try {
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(ApiResponse.builder()
                            .code(HttpStatus.ACCEPTED.value())
                            .data(userService.updateUser(request, getCurrentUserId()))
                            .build());
        }catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(ApiResponse.builder()
                            .code(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .build());
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(ApiResponse.builder()
                            .code(HttpStatus.BAD_REQUEST.value())
                            .message(e.getMessage())
                            .build());
        }catch (AlreadyExistsException e){
            return ResponseEntity.status(HttpStatus.IM_USED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(ApiResponse.builder()
                            .code(HttpStatus.IM_USED.value())
                            .message(e.getMessage())
                            .build());
        }
    }
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser() {
        try {
            return ResponseEntity.status(HttpStatus.FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(ApiResponse.<UserResponse>builder()
                            .code(HttpStatus.FOUND.value())
                            .data(userService.getCurrentUser(getCurrentUserId()))
                            .build());
        }catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(ApiResponse.<UserResponse>builder()
                            .code(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .build());
        }
    }
}