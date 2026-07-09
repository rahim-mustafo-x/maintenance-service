package org.safa.maintenanceservice.models.dto.user.auth;

public record SendCodeRequest(
    String refreshToken,
    long userId
) {}
