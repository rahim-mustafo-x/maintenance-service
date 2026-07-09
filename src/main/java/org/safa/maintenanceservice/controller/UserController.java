package org.safa.maintenanceservice.controller;

import org.safa.maintenanceservice.models.dto.ResponseBody;
import org.safa.maintenanceservice.models.dto.user.auth.ChangePasswordRequest;
import org.safa.maintenanceservice.models.dto.user.auth.SendCodeRequest;
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
    public ResponseEntity<ResponseBody<Boolean>> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        //todo fix here
        throw new RuntimeException("Implement this position after building a new tg bot");
    }
    @DeleteMapping("/delete-user")
    public ResponseEntity<ResponseBody<Boolean>> deleteUser(@RequestParam long userId) {
        try {
            if (userService.deleteUser(userId)){
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new ResponseBody<>(HttpStatus.OK.value(), true));
            }else {
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new ResponseBody<>(HttpStatus.NOT_FOUND.value(), "User has already been deleted"));
            }
        } catch (Exception e) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ResponseBody<>(HttpStatus.BAD_REQUEST.value(), false, e.getMessage()));
        }
    }
    @PatchMapping("/sendChangeCode")
    public ResponseEntity<ResponseBody<Boolean>> changeUserPassword(@RequestBody SendCodeRequest sendCodeRequest) {
        try {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ResponseBody<>(HttpStatus.OK.value(), userService.sendCode(sendCodeRequest)));
        }catch (Exception e){
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new  ResponseBody<>(HttpStatus.BAD_REQUEST.value(), false, e.getMessage()));
        }
    }
}