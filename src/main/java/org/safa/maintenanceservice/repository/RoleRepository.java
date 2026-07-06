package org.safa.maintenanceservice.repository;

import org.safa.maintenanceservice.models.entity.user.role.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

}
