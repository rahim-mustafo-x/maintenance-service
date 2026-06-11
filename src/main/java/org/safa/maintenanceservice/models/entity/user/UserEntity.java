package org.safa.maintenanceservice.models.entity.user;

import jakarta.persistence.*;
import lombok.*;

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
    @Column(name = "role")
    private String role;
    @Column(name = "latitude")
    private double latitude;
    @Column(name = "longitude")
    private double longitude;

    /**This is for register**/
    public UserEntity(String fullName, String username, String password, String phoneNumber, String role, double latitude, double longitude) {
        this.fullName = fullName;
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    /** This is for patch requests **/
    public UserEntity(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /** This part is for changing some data in a place called settings **/
    public UserEntity(Long id, String fullName, String username, String password, String phoneNumber) {
        this.id = id;
        this.fullName = fullName;
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }
}