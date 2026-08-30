package org.safa.maintenanceservice.user.model.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CodeRequest(
        @JsonProperty("code")
        String code,
        @JsonProperty("phone_number")
        String phoneNumber
) {}
