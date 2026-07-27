package org.safa.maintenanceservice.models.dto.labor;

import java.time.LocalTime;

public record SetWorkingHoursRequest(
        LocalTime startWorking,
        LocalTime endWorking
) {}
