package org.safa.maintenanceservice.labor.model.dto.labor

import org.safa.maintenanceservice.labor.model.model.LaborType

data class SearchLaborResponse(
    val id: Long,
    val laborTypes: Set<LaborType>,
    val name: String
)