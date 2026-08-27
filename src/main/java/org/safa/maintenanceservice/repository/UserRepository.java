package org.safa.maintenanceservice.repository;

import jakarta.transaction.Transactional;
import org.safa.maintenanceservice.models.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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
    Optional<UserEntity> findByUsername(@Param("username") String username);

    /** we have to add exists by unique things such as username phone number  in order to avoid 403**/
    @Query("select count(u) > 0 from UserEntity u where u.username=:username")
    boolean existsByUsername(@Param("username") String username);

    @Query("select count(u) > 0 from UserEntity u where u.phoneNumber=:phoneNumber")
    boolean existsByPhoneNumber(@Param("phoneNumber") String phoneNumber);

    @Modifying
    @Transactional
    @Query("update UserEntity u set u.password=:password where u.id=:userId")
    void changePassword(@Param("userId") long userId, @Param("password") String password);

    @Query("select u from UserEntity u where u.phoneNumber=:phoneNumber")
    Optional<UserEntity> findByPhoneNumber(@Param("phoneNumber") String phoneNumber);

    @Transactional
    @Modifying
    @Query("update UserEntity u set u.fullName=:fullName, u.username=:username, u.phoneNumber=:phoneNumber where u.id=:userId")
    void updateByUserId(@Param("fullName") String fullName, @Param("username") String username, @Param("phoneNumber") String phoneNumber, @Param("userId")  long userId);
}