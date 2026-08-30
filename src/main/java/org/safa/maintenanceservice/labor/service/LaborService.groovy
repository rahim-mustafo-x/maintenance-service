package org.safa.maintenanceservice.labor.service

import org.safa.maintenanceservice.labor.model.dto.labor.LaborCreateRequest
import org.safa.maintenanceservice.labor.model.dto.labor.LaborResponse
import org.safa.maintenanceservice.labor.model.dto.labor.SearchLaborResponse
import org.safa.maintenanceservice.labor.model.model.LaborType
import org.springframework.data.domain.Page

interface LaborService {
    boolean saveWorkingHours(LaborCreateRequest laborCreateRequest, long userId)

    Page<SearchLaborResponse> searchLabors(LaborType type, int page, int size)

    LaborResponse laborById(long userId)
}