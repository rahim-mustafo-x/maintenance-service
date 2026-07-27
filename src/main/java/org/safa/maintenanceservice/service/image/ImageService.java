package org.safa.maintenanceservice.service.image;

import org.safa.maintenanceservice.models.dto.image.ImageByteResponse;
import org.safa.maintenanceservice.models.dto.image.ImageResponse;
import org.safa.maintenanceservice.models.entity.image.ImageEntity;
import org.safa.maintenanceservice.models.exceptions.NotFoundException;
import org.safa.maintenanceservice.models.model.ImageType;
import org.safa.maintenanceservice.repository.ImageRepository;
import org.safa.maintenanceservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
public class ImageService {
    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private UserRepository userRepository;

    public ImageResponse save(MultipartFile file, ImageType imageType, long ownerId) throws IOException {
        if (notExistsByOwnerId(ownerId)){
            throw new NotFoundException("Owner id not found");
        }
        UUID imageId;
        if (imageType != ImageType.LABOR_WORK){
            imageId = imageRepository.findIdByOwnerIdAndImageType(ownerId, imageType).orElse(UUID.randomUUID());
        }else {
            imageId = UUID.randomUUID();
        }
        ImageEntity save = imageRepository.save(new ImageEntity(imageId, file.getBytes(), file.getContentType(), file.getOriginalFilename(), imageType, ownerId));
        return new ImageResponse(save.getId(), save.getContentType(), save.getFileName(), save.getOwnerId(), save.getImageType());
    }

    public ImageByteResponse getImageAsByteArray(UUID id) {
        Optional<ImageEntity> imageEntity = imageRepository.findById(id);
        if (imageEntity.isPresent()) {
            var image = imageEntity.get();
            return new ImageByteResponse(image.getData(), image.getContentType());
        }
        throw new NotFoundException("Image not found");
    }

    public ImageResponse getImageResponse(UUID id) {
        Optional<ImageEntity> imageEntity = imageRepository.findById(id);
        if (imageEntity.isPresent()) {
            var image = imageEntity.get();
            return new ImageResponse(image.getId(), image.getContentType(), image.getFileName(), image.getOwnerId(), image.getImageType());
        }
        throw new NotFoundException("Image not found");
    }

    public ImageResponse updateImage(MultipartFile file, UUID id, long ownerId) throws IOException {
        Optional<ImageEntity> imageEntity = imageRepository.findById(id);
        if (imageEntity.isEmpty()) {
            throw new NotFoundException("Image not found");
        }
        if (notExistsByOwnerId(ownerId)){
            throw new NotFoundException("Owner id not found");
        }
        var image = imageEntity.get();
        image.setData(file.getBytes());
        image.setContentType(file.getContentType());
        image.setFileName(file.getOriginalFilename());
        ImageEntity save = imageRepository.save(image);
        return new ImageResponse(save.getId(), save.getContentType(), save.getFileName(), image.getOwnerId(), image.getImageType());
    }

    public boolean deleteImage(UUID id) {
        Optional<ImageEntity> imageEntity = imageRepository.findById(id);
        if (imageEntity.isEmpty()) {
            throw new NotFoundException("Image not found");
        }
        imageRepository.delete(imageEntity.get());
        return true;
    }

    //todo implement deployment id concept as well
    private boolean notExistsByOwnerId(long ownerId) {
        //check if ownerId exists
        return !userRepository.existsById(ownerId);
    }
}