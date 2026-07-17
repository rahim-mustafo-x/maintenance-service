package org.safa.maintenanceservice.repository;

import org.safa.maintenanceservice.models.entity.image.ImageEntity;
import org.safa.maintenanceservice.models.model.ImageType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ImageRepository extends JpaRepository<ImageEntity, UUID> {
    @Query("select i.id from ImageEntity i where i.ownerId=:ownerId and i.imageType=:imageType")
    Optional<UUID> findIdByOwnerIdAndImageType(@Param("ownerId") long ownerId, @Param("imageType") ImageType imageType);
}
