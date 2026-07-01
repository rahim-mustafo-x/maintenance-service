package org.safa.maintenanceservice.models.dto.user.auth;

public record ChangePasswordRequest(
        String code,
        String newPassword
) {}