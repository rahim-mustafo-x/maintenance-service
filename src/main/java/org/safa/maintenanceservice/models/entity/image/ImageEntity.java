package org.safa.maintenanceservice.models.entity.image;

import jakarta.persistence.*;
import lombok.*;
import org.safa.maintenanceservice.models.model.ImageType;
import java.util.UUID;

@Entity
@Table(name = "image")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ImageEntity {
    @Id
    private UUID id;
    @Lob
    @Column(nullable = false)
    private byte[] data;
    @Column(nullable = false)
    private String contentType;
    @Column(nullable = false, name = "fileName")
    private String fileName;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ImageType imageType;
    /**
     * check this field manually every updating,inserting and deleting process of user as well as image*/
    private long ownerId;

    public ImageEntity(byte[] data, String contentType, String fileName, ImageType imageType, long ownerId) {
        this.id = UUID.randomUUID();
        this.data = data;
        this.contentType = contentType;
        this.fileName = fileName;
        this.imageType = imageType;
        this.ownerId = ownerId;
    }
}