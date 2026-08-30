package org.safa.maintenanceservice.user.model.dto.auth.login;

import org.safa.maintenanceservice.user.model.UserRole;

public record LoginUserRequest(
        String username,
        String password,
        UserRole role
){}