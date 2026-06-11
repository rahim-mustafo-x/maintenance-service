package org.safa.maintenanceservice.models.dto.user.auth.login;

public record LoginUserRequest(
        String username,
        String password
){}