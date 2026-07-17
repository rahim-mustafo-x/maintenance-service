package org.safa.maintenanceservice.controller.image;

import org.safa.maintenanceservice.models.dto.ResponseBody;
import org.safa.maintenanceservice.models.dto.image.ImageByteResponse;
import org.safa.maintenanceservice.models.dto.image.ImageResponse;
import org.safa.maintenanceservice.models.exceptions.NotFoundException;
import org.safa.maintenanceservice.models.model.ImageType;
import org.safa.maintenanceservice.service.image.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.awt.*;
import java.util.UUID;

@RestController
@RequestMapping("/v1/image")
public class ImageController {
    @Autowired
    private ImageService imageService;

    @PostMapping
    public ResponseEntity<ResponseBody<ImageResponse>> saveImage(@RequestPart MultipartFile file, @RequestParam long ownerId, @RequestParam ImageType imageType) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ResponseBody<>(HttpStatus.CREATED.value(), imageService.save(file, imageType, ownerId), null));
        }catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ResponseBody<>(HttpStatus.NOT_FOUND.value(), null, e.getMessage()));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON).body(new ResponseBody<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, e.getMessage()));
        }
    }

    @GetMapping("/{imageId}")
    public ResponseEntity<byte[]> getAllImages(@PathVariable UUID imageId) {
        try {
            ImageByteResponse imageAsByteArray = imageService.getImageAsByteArray(imageId);
            return ResponseEntity.status(HttpStatus.FOUND)
                    .contentType(MediaType.parseMediaType(imageAsByteArray.contentType()))
                    .body(imageAsByteArray.data());
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.MULTIPART_FORM_DATA).body(null);
        }
    }

    @GetMapping("/get-data/{imageId}")
    public ResponseEntity<ResponseBody<ImageResponse>> getImageData(@PathVariable UUID imageId) {
        try {
            return ResponseEntity.status(HttpStatus.FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ResponseBody<>(HttpStatus.FOUND.value(), imageService.getImageResponse(imageId)));
        }catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.MULTIPART_FORM_DATA).body(new ResponseBody<>(HttpStatus.NOT_FOUND.value(), null, e.getMessage()));
        }
    }
    @PutMapping
    public ResponseEntity<ResponseBody<ImageResponse>> updateImage(@RequestPart MultipartFile file, @RequestParam UUID imageId, @RequestParam long ownerId) {
        try {
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ResponseBody<>(HttpStatus.ACCEPTED.value(), imageService.updateImage(file, imageId, ownerId)));
        }catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.MULTIPART_FORM_DATA).body(new  ResponseBody<>(HttpStatus.NOT_FOUND.value(), null, e.getMessage()));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON).body(new ResponseBody<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, e.getMessage()));
        }
    }
    @DeleteMapping("/{imageId}")
    public ResponseEntity<ResponseBody<Boolean>> deleteImage(@PathVariable UUID imageId) {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ResponseBody<>(HttpStatus.OK.value(), imageService.deleteImage(imageId)));
        }catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ResponseBody<>(HttpStatus.NOT_FOUND.value(), null, e.getMessage()));
        }
    }
}
