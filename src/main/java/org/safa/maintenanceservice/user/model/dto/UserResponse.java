package org.safa.maintenanceservice.user.model.dto;

import java.util.Set;

/**This class also can be stored and may be can be extended**/
public record UserResponse(
        long id,
        String fullName,
        String userName,
        String phoneNumber,
        Set<String> roles
) {}