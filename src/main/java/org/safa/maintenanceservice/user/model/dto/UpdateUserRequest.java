package org.safa.maintenanceservice.user.model.dto;

public record UpdateUserRequest(
        String fullName,
        String username,
        String phoneNumber
) {}
