package org.safa.maintenanceservice.models.dto.user;

import java.util.Set;

public record MaintenanceUser(
        String userName,
        Set<String> roles,
        String password
) {}
