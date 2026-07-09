package org.safa.maintenanceservice.service;

import jakarta.validation.constraints.NotNull;
import org.safa.maintenanceservice.config.RedisConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Service
public class CodeService {
    //code is saved for 2 minutes by default
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void saveCode(long userId, String deviceId, int code){
        redisTemplate.opsForValue().set(codeKeyGenerator(userId, deviceId), code, 2, TimeUnit.MINUTES);
    }

    public String getCode(long userId, String deviceId){
        return (String) redisTemplate.opsForValue().get(codeKeyGenerator(userId, deviceId));
    }

    public void deleteCode(long userId, String deviceId){
        redisTemplate.delete(codeKeyGenerator(userId, deviceId));
    }

    private String codeKeyGenerator(long userId, String deviceId) {
        return "code_" + userId+"_"+deviceId;
    }
}
