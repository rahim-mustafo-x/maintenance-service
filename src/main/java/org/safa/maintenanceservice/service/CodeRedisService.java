package org.safa.maintenanceservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

@Service
public class CodeRedisService {
    //code is saved for 2 minutes by default
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void saveCodeFor2Minutes(long userId, String phoneNumber, String code){
        redisTemplate.opsForValue().set(codeKeyGenerator(userId, phoneNumber), code, 2, TimeUnit.MINUTES);
    }

    public String getCode(long userId, String phoneNumber){
        return (String) redisTemplate.opsForValue().get(codeKeyGenerator(userId, phoneNumber));
    }

    public void deleteCode(long userId, String phoneNumber){
        redisTemplate.delete(codeKeyGenerator(userId, phoneNumber));
    }

    private String codeKeyGenerator(long userId, String phoneNumber) {
        return "code_" + userId+"_"+phoneNumber;
    }
    public String generateCode(int length){
        if (length<=0){
            throw new IllegalArgumentException("length should be greater than 0");
        }else {
            var code = new StringBuilder();
            var random = new SecureRandom();
            for (int i = 0; i < length; i++) {
                code.append(random.nextInt(0,10));
            }
            return code.toString();
        }
    }

    public boolean isExpired(long userId, String phoneNumber) {
        var remainingTime = redisTemplate.getExpire(codeKeyGenerator(userId, phoneNumber), TimeUnit.SECONDS);
        return remainingTime == -2;
    }
}
