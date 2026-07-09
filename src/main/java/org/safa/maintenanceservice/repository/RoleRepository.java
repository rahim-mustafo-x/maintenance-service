package org.safa.maintenanceservice.repository;

import org.safa.maintenanceservice.models.entity.user.role.RoleEntity;
import org.safa.maintenanceservice.models.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    @Query("select r from RoleEntity r where r.role=:role and r.user.id=:userId")
    Optional<RoleEntity> findByRoleAndUserId(@Param("role") UserRole role, @Param("userId") long userId);
}
