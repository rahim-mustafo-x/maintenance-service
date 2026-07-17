package org.safa.maintenanceservice.models.entity.user.session;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import java.time.Duration;
import java.util.UUID;

@RedisHash("sessions")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SessionEntity {
    @Id
    private String id;
    @Indexed
    private long userId;
    @Indexed
    private String refreshToken;
    @TimeToLive
    private long timeToLive;

    public SessionEntity(long userId, String refreshToken) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.refreshToken = refreshToken;
        this.timeToLive = Duration.ofDays(30).toSeconds();
    }
}
