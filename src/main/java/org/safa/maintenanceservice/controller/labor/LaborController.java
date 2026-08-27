package org.safa.maintenanceservice.controller.labor;

import org.safa.maintenanceservice.models.dto.ApiResponse;
import org.safa.maintenanceservice.models.dto.labor.SetWorkingHoursRequest;
import org.safa.maintenanceservice.service.labor.LaborService;
import org.safa.maintenanceservice.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("v1/labor")
public class LaborController {
    @Autowired
    private LaborService laborService;
    @Autowired
    private UserService userService;
    private long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = Objects.requireNonNull(authentication).getName();
        return userService.findUserIdByUserName(username);
    }

    @PostMapping("/set-working-hours")
    public ResponseEntity<ApiResponse<?>>  setWorkingHours(@RequestBody SetWorkingHoursRequest setWorkingHoursRequest){
        return null;
    }
}
