package org.safa.maintenanceservice.models.dto.user.auth.register;

import org.safa.maintenanceservice.models.model.UserLocation;
import org.safa.maintenanceservice.models.model.UserRole;

public record RegisterUserRequest(
        String fullName,
        String userName,
        String phoneNumber,
        String password,
        UserRole role,
        UserLocation location
) {
}
