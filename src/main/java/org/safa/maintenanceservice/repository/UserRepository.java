package org.safa.maintenanceservice.repository;

import org.jspecify.annotations.NonNull;
import org.safa.maintenanceservice.models.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    /**
     * In here we are taking user model via user_name.
     * It is considered to be used only when login formats like UserDetailsService**/
    @Query("select u from UserEntity u where u.username=:username")
    Optional<UserEntity> findByUsername(@NonNull @Param("username") String username);

    /** we have to add exists by unique things such as username phone number  in order to avoid 403**/
    @Query("select count(u) > 0 from UserEntity u where u.username=:username")
    boolean existsByUsername(@NonNull @Param("username") String username);

    @Query("select count(u) > 0 from UserEntity u where u.phoneNumber=:phoneNumber")
    boolean existsByPhoneNumber(@NonNull @Param("phoneNumber") String phoneNumber);
}
