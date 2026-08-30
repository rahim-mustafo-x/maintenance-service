package org.safa.maintenanceservice.labor.model.dto.labor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.safa.maintenanceservice.labor.model.dto.workingHours.WorkingHourResponse;
import org.safa.maintenanceservice.labor.model.model.LaborType;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LaborResponse {
    private Set<LaborType> laborTypes;
    private Long id;
    private Long userId;
    private String fullName;
    private Set<WorkingHourResponse> workingHours;
}
