package org.safa.maintenanceservice.models.dto.user.auth.register;

import org.safa.maintenanceservice.models.model.UserRole;

public record RegisterUserRequest(
        String fullName,
        String username,
        String phoneNumber,
        String password,
        UserRole role
) {
}
