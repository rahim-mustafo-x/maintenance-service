package org.safa.maintenanceservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class TokenService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void saveRefreshToken(long userId, String refreshToken){
        var key = generateTokenKey(userId);
        //used ttl to auto remove the token after 30 days
        redisTemplate.opsForValue().set(key, refreshToken, 30, TimeUnit.DAYS);
    }

    public void saveUserIdToken(String refreshToken, long userId){
        var key = generateUserIdKey(refreshToken);
        //used ttl to auto remove the token after 30 days
        redisTemplate.opsForValue().set(key, userId, 30, TimeUnit.DAYS);
    }

    public String getRefreshToken(long userId) throws NullPointerException {
        return Objects.requireNonNull(redisTemplate.opsForValue().get(generateTokenKey(userId))).toString();
    }

    public long getUserId(String refreshToken) throws NullPointerException {
        return Objects.requireNonNull(redisTemplate.opsForValue().get(generateUserIdKey(refreshToken))).hashCode();
    }

    private String generateTokenKey(long userId) {
        return "token::refreshToken::"+userId;
    }

    private String generateUserIdKey(String refreshToken) {
        return "refreshToken::userId::"+refreshToken;
    }

    //we use log-out to manually let the user remove one's own token
    public void deleteUserId(long userId) {
        redisTemplate.delete(generateTokenKey(userId));
    }

    public void deleteUserId(String refreshToken) {
        redisTemplate.delete(generateUserIdKey(refreshToken));
    }
}
