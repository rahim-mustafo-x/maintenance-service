package org.safa.maintenanceservice.models.entity.labor;

import jakarta.persistence.*;
import lombok.*;
import org.safa.maintenanceservice.models.entity.user.UserEntity;
import org.safa.maintenanceservice.models.model.LaborStatus;
import java.time.LocalTime;

@Entity
@Table(name = "labor")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LaborEntity {
    /**
     * <pre>
     * user_id
     * name
     * phone_number
     *
     * image_uuid_id is from by user
     *
     * startWorking
     * endWorking
     * </pre>*/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    private UserEntity user;
    @Enumerated(EnumType.STRING)
    private LaborStatus status;

    //start to work till end
    private LocalTime startWorking;
    private LocalTime endWorking;
}