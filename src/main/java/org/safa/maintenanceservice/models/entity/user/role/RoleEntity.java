package org.safa.maintenanceservice.models.entity.user.role;

import jakarta.persistence.*;
import lombok.*;
import org.safa.maintenanceservice.models.entity.user.UserEntity;
import org.safa.maintenanceservice.models.model.UserRole;

@Entity
@Table(name="roles")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Enumerated(EnumType.STRING)
    private UserRole role;
    @ManyToOne
    @JoinColumn(name="user_id")
    private UserEntity user;

    public RoleEntity(UserRole role, UserEntity user) {
        this.role = role;
        this.user = user;
    }
}