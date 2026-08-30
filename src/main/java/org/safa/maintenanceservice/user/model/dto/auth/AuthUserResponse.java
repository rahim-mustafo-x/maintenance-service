package org.safa.maintenanceservice.user.model.dto.auth;

public record AuthUserResponse(
        String token,
        String refreshToken
) {}
