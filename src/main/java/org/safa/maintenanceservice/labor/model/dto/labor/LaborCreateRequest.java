package org.safa.maintenanceservice.labor.model.dto.labor;

import org.safa.maintenanceservice.labor.model.dto.workingHours.WorkingHoursCreateRequest;
import org.safa.maintenanceservice.labor.model.model.LaborType;
import java.util.Set;

public record LaborCreateRequest(
        Set<WorkingHoursCreateRequest> workingHoursRequests,
        Set<LaborType> laborTypes
) {}
