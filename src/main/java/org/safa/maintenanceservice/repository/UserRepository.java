package org.safa.maintenanceservice.repository;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import org.jspecify.annotations.NonNull;
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
    Optional<UserEntity> findByUsername(@NonNull @Param("username") String username);

    @Query("select u from UserEntity u where u.id = :userId")
    Optional<UserEntity> findByUsernameId(@Param("userId") long userId);

    /** we have to add exists by unique things such as username phone number  in order to avoid 403**/
    @Query("select count(u) > 0 from UserEntity u where u.username=:username")
    boolean existsByUsername(@NonNull @Param("username") String username);

    @Query("select count(u) > 0 from UserEntity u where u.phoneNumber=:phoneNumber")
    boolean existsByPhoneNumber(@NonNull @Param("phoneNumber") String phoneNumber);

    @Modifying
    @Transactional
    @Query("update UserEntity u set u.password=:password where u.id=:userId")
    void changePassword(@Param("userId") long userId, @NonNull @Param("password") String password);

    @Query("select u from UserEntity u where u.phoneNumber=:phoneNumber")
    Optional<UserEntity> findByPhoneNumber(@NotNull @Param("phoneNumber") String phoneNumber);

    @Transactional
    @Modifying
    @Query("update UserEntity u set u.fullName=:fullName, u.username=:username, u.phoneNumber=:phoneNumber where u.id=:userId")
    void updateByUserId(@NotNull @Param("fullName") String fullName, @NotNull @Param("username") String username, @NotNull @Param("phoneNumber") String phoneNumber, @Param("userId")  long userId);
}