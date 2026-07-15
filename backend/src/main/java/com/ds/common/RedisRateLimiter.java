package com.ds.common;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisRateLimiter {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final int MAX_REQUESTS = 10;
    private static final int EXPIRE_SECONDS = 1;

    public RedisRateLimiter(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean isAllowed(String key) {
        String redisKey = "rate:" + key;
        Long count = redisTemplate.opsForValue().increment(redisKey);

        if (count != null && count == 1) {
            redisTemplate.expire(redisKey, EXPIRE_SECONDS, TimeUnit.SECONDS);
        }

        if (count != null && count <= MAX_REQUESTS) {
            return true;
        }

        return false;
    }
}