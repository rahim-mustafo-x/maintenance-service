package org.safa.maintenanceservice.service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimitService {
    private final Map<String, Bucket> strictBuckets = new ConcurrentHashMap<>();
    private final Map<String, Bucket> regularBuckets = new ConcurrentHashMap<>();
    private final Map<String, Bucket> scrollBuckets = new ConcurrentHashMap<>();

    /** This session key is gained from HttpServletRequest**/
    public Bucket resolveStrictBucket(String sessionKey){
        return strictBuckets.computeIfAbsent(sessionKey, _->Bucket.builder().addLimit(
                Bandwidth.builder().capacity(20).refillIntervally(10, Duration.ofMinutes(1)).build()
        ).build());
    }
    public Bucket resolveRegularBucket(String sessionKey){
        return regularBuckets.computeIfAbsent(sessionKey, _->Bucket.builder().addLimit(
                Bandwidth.builder().capacity(100).refillIntervally(100, Duration.ofMinutes(1)).build()
        ).build());
    }
    public Bucket resolveScrollBucket(String sessionKey){
        return scrollBuckets.computeIfAbsent(sessionKey, _->Bucket.builder().addLimit(
                Bandwidth.builder().capacity(20).refillIntervally(20, Duration.ofMinutes(1)).build()
        ).build());
    }
}