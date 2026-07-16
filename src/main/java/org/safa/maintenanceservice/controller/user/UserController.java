package org.safa.maintenanceservice.controller.user;

import org.safa.maintenanceservice.models.dto.ResponseBody;
import org.safa.maintenanceservice.models.dto.user.UpdateUserRequest;
import org.safa.maintenanceservice.models.dto.user.UserResponse;
import org.safa.maintenanceservice.models.exceptions.AlreadyExistsException;
import org.safa.maintenanceservice.models.exceptions.BadRequestException;
import org.safa.maintenanceservice.models.exceptions.NotFoundException;
import org.safa.maintenanceservice.service.user.UserService;
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
        return userService.findByUserName(username);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseBody<Boolean>> deleteUser() {
        try {
            if (userService.deleteUser(getCurrentUserId())){
                return ResponseEntity.status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new ResponseBody<>(HttpStatus.OK.value(), true));
            }else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new ResponseBody<>(HttpStatus.NOT_FOUND.value(), "User has already been deleted"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ResponseBody<>(HttpStatus.BAD_REQUEST.value(), false, e.getMessage()));
        }
    }


    @PutMapping("update")
    public ResponseEntity<ResponseBody<Boolean>> updateUser(@RequestBody UpdateUserRequest request){
        try {
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ResponseBody<>(HttpStatus.ACCEPTED.value(), userService.updateUser(request, getCurrentUserId()), null));
        }catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new  ResponseBody<>(HttpStatus.NOT_FOUND.value(), false, e.getMessage()));
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new  ResponseBody<>(HttpStatus.BAD_REQUEST.value(), false, e.getMessage()));
        }catch (AlreadyExistsException e){
            return ResponseEntity.status(HttpStatus.IM_USED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new  ResponseBody<>(HttpStatus.IM_USED.value(), false, e.getMessage()));
        }
    }
    @GetMapping("/me")
    public ResponseEntity<ResponseBody<UserResponse>> getCurrentUser() {
        try {
            return ResponseEntity.status(HttpStatus.FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ResponseBody<>(HttpStatus.FOUND.value(), userService.getCurrentUser(getCurrentUserId()), null));
        }catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ResponseBody<>(HttpStatus.NOT_FOUND.value(), null, e.getMessage()));
        }
    }
}