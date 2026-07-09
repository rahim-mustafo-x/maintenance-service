package org.safa.maintenanceservice.models.dto.user.auth.login;

import org.safa.maintenanceservice.models.model.UserRole;

public record LoginUserRequest(
        String username,
        String password,
        UserRole role
){}