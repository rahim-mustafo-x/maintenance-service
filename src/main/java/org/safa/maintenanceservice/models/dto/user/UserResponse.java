package org.safa.maintenanceservice.models.dto.user;

import java.util.Set;

/**This class also can be stored and may be can be extended**/
public record UserResponse(
        long id,
        String fullName,
        String userName,
        String phoneNumber,
        Set<String> roles
) {}