package org.safa.maintenanceservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.safa.maintenanceservice.models.dto.ResponseBody;
import org.safa.maintenanceservice.models.dto.user.auth.ChangePasswordRequest;
import org.safa.maintenanceservice.models.exceptions.BadRequestException;
import org.safa.maintenanceservice.models.exceptions.ExpiredException;
import org.safa.maintenanceservice.models.exceptions.NotFoundException;
import org.safa.maintenanceservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PatchMapping("/changePassword")
    public ResponseEntity<ResponseBody<Boolean>> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest, HttpServletRequest request) {
        try {
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ResponseBody<>(HttpStatus.ACCEPTED.value(), userService.changePassword(changePasswordRequest, request), null));
        }catch (ExpiredException e){
            return ResponseEntity.status(HttpStatus.GONE)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ResponseBody<>(HttpStatus.GONE.value(), false, e.getMessage()));
        }catch (BadRequestException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ResponseBody<>(HttpStatus.BAD_REQUEST.value(), false, e.getMessage()));

        }catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ResponseBody<>(HttpStatus.NOT_FOUND.value(), false, e.getMessage()));
        }
    }

    @PostMapping("/sendChangeCode")
    public ResponseEntity<ResponseBody<Boolean>> sendChangeCode(HttpServletRequest request) {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ResponseBody<>(HttpStatus.OK.value(), userService.sendCode(request)));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new  ResponseBody<>(HttpStatus.BAD_REQUEST.value(), false, e.getMessage()));
        }
    }

    @DeleteMapping("/delete-user")
    public ResponseEntity<ResponseBody<Boolean>> deleteUser(@RequestParam long userId) {
        try {
            if (userService.deleteUser(userId)){
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
}