package org.safa.maintenanceservice.models.entity.user;

import jakarta.persistence.*;
import lombok.*;
import org.safa.maintenanceservice.models.entity.labor.LaborEntity;
import org.safa.maintenanceservice.models.entity.user.role.RoleEntity;

import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {

    /**
     This is an entity dedicated to storing and gathering user in maintenance database.
      **/

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "fullName", nullable = false)
    private String fullName;
    @Column(name = "username", unique = true, nullable = false)
    private String username;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "phoneNumber", unique = true)
    private String phoneNumber;
    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    private Set<RoleEntity> roles;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private LaborEntity labor;

    /**This is for register**/
    public UserEntity(String fullName, String username, String password, String phoneNumber) {
        this.fullName = fullName;
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

}