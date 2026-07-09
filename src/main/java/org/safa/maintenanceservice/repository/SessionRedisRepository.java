package org.safa.maintenanceservice.repository;

import org.safa.maintenanceservice.models.entity.user.session.SessionEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
public interface SessionRedisRepository extends CrudRepository<SessionEntity, Long> {
    void delete(long userId);
    void deleteByRefreshToken(String refreshToken);
    SessionEntity findByRefreshToken(String refreshToken);
}
