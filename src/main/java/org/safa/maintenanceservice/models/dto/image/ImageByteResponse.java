package org.safa.maintenanceservice.models.dto.image;

public record ImageByteResponse(
        byte[] data,
        String contentType
) {}
