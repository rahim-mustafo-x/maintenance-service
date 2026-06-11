package org.safa.maintenanceservice.models.dto.user.auth;

public record AuthUserResponse(
        String token,
        String refreshToken
) {}
