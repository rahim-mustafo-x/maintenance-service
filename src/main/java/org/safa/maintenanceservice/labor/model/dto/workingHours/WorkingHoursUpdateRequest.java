package org.safa.maintenanceservice.labor.model.dto.workingHours;

import java.time.DayOfWeek;
import java.time.LocalTime;

public record WorkingHoursUpdateRequest(
        DayOfWeek day,
        LocalTime startTime,
        LocalTime endTime
) {}