package org.safa.maintenanceservice.models.entity.image;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
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
    @Column(nullable = false, name = "data")
    private byte[] data;
    @Column(nullable = false, name = "creationDate")
    private Instant creationDate;
    @Column(nullable = false, name = "contentType")
    private String contentType;
    @Column(nullable = false, name = "fileName")
    private String fileName;
}
