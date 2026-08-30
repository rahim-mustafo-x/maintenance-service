package org.safa.maintenanceservice.user.model.dto.auth.register;

import org.safa.maintenanceservice.user.model.UserRole;

public record RegisterUserRequest(
        String fullName,
        String username,
        String phoneNumber,
        String password,
        UserRole role
) {
}
