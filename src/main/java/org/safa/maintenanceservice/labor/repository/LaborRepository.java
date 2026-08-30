package org.safa.maintenanceservice.labor.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.safa.maintenanceservice.labor.model.entity.LaborEntity;
import org.safa.maintenanceservice.labor.model.model.LaborType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LaborRepository extends JpaRepository<LaborEntity, Long> {

    @Query("select l from LaborEntity l where l.user.id=:userId")
    Optional<LaborEntity> findByUserId(@Param("userId") Long userId);

    @Query("select l from LaborEntity l where :type member of l.laborTypes")
    Page<LaborEntity> findAllByType(LaborType type, PageRequest pageable);
}
