package org.safa.maintenanceservice.models.dto.user;

public record UpdateUserRequest(
        String fullName,
        String username,
        String phoneNumber
) {}
