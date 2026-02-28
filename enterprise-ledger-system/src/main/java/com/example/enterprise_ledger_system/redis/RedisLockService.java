package com.example.enterprise_ledger_system.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisLockService {

    private final StringRedisTemplate redisTemplate;

    public boolean acquireIdempotencyLock(String idempotencyKey, String requestHash){
        Boolean acquired = redisTemplate.opsForValue().setIfAbsent(
                "idemp_lock:"+idempotencyKey,
                requestHash,
                Duration.ofHours(24)
        );

        return Boolean.TRUE.equals(acquired);
    }

    public String getExistingHash(String idempotencyKey){
        return redisTemplate.opsForValue().get("idemp_lock:"+idempotencyKey);
    }
}
