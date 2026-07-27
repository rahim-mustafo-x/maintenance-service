package org.safa.maintenanceservice.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.safa.maintenanceservice.models.entity.labor.LaborEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LaborRepository extends JpaRepository<LaborEntity, Long> {

    @Query("select l from LaborEntity l where l.user.id=:userId")
    Optional<LaborEntity> findByUserId(@Param("userId") Long userId);
}
