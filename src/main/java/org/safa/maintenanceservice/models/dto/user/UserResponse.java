package org.safa.maintenanceservice.models.dto.user;

import org.safa.maintenanceservice.models.model.UserLocation;
import org.safa.maintenanceservice.models.model.UserRole;

public record UserResponse(
        long id,
        String fullName,
        String userName,
        String phoneNumber,
        UserRole role,
        UserLocation location
) {}
