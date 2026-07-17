package org.safa.maintenanceservice.models.dto.image;

import org.safa.maintenanceservice.models.model.ImageType;

import java.util.UUID;

public record ImageResponse(
        UUID id,
        String contentType,
        String fileName,
        long ownerId,
        ImageType imageType
) {}