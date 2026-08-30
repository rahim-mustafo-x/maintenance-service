package org.safa.maintenanceservice.labor.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.safa.maintenanceservice.user.model.entity.UserEntity;
import org.safa.maintenanceservice.labor.model.model.LaborType;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "labor")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class LaborEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    private UserEntity user;
    @ElementCollection
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Set<LaborType> laborTypes = new HashSet<>();
    //                                    | effecting all entity    | when removed from list auto removed by entity as well
    @OneToMany(mappedBy = "laborEntity",  cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<WorkingHoursEntity> workingHours;
}