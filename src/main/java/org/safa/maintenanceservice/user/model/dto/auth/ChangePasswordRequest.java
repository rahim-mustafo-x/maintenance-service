package org.safa.maintenanceservice.user.model.dto.auth;

public record ChangePasswordRequest(
        String code,
        String newPassword
) {}