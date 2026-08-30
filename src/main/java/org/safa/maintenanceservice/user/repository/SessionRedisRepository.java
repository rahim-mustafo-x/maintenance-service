package org.safa.maintenanceservice.user.repository;

import org.safa.maintenanceservice.user.model.entity.session.SessionEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
public interface SessionRedisRepository extends CrudRepository<SessionEntity, Long> {
    void deleteByRefreshToken(String refreshToken);
    void deleteByUserId(long userId);
    SessionEntity findByRefreshToken(String refreshToken);
}
