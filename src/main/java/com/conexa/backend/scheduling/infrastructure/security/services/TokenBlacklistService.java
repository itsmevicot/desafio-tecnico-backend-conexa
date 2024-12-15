package com.conexa.backend.scheduling.infrastructure.security.services;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class TokenBlacklistService {

    private final RedisTemplate<String, String> redisTemplate;

    public TokenBlacklistService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void invalidateToken(String token, Duration expiration) {
        ValueOperations<String, String> valueOps = redisTemplate.opsForValue();
        valueOps.set(token, "blacklisted", expiration);
    }

    public boolean isTokenBlacklisted(String token) {
        ValueOperations<String, String> valueOps = redisTemplate.opsForValue();
        return valueOps.get(token) != null;
    }
}
