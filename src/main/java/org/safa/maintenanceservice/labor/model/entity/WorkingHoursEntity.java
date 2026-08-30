package org.safa.maintenanceservice.labor.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
@Table(name = "workingHours")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class WorkingHoursEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private DayOfWeek day;
    private LocalTime startTime;
    private LocalTime endTime;
    //        | best for performance for a list to get the same senior instead of different with same id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "labor")
    private LaborEntity laborEntity;
}
