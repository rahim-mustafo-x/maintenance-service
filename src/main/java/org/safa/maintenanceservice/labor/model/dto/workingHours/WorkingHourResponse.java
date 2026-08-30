package org.safa.maintenanceservice.labor.model.dto.workingHours;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WorkingHourResponse {
    private LocalTime startTime;
    private Long id;
    private LocalTime endTime;
    private DayOfWeek day;
}