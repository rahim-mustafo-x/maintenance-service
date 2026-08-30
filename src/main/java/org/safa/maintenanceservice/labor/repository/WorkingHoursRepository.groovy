package org.safa.maintenanceservice.labor.repository

import org.safa.maintenanceservice.labor.model.entity.WorkingHoursEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface WorkingHoursRepository extends JpaRepository<WorkingHoursEntity, Long>{}