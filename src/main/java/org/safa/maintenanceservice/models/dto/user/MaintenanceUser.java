package org.safa.maintenanceservice.models.dto.user;

import org.safa.maintenanceservice.models.model.UserRole;

public record MaintenanceUser(
        String userName,
        UserRole role,
        String password
) {}
