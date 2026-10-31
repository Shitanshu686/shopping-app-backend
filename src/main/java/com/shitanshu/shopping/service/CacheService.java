package com.shitanshu.shopping.service;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class CacheService {

    private static final Logger log = LoggerFactory.getLogger(CacheService.class);

    @Autowired(required = false)
    private StringRedisTemplate redisTemplate;

    public void cacheData(String key, String value, long timeoutInSeconds) {
        if (redisTemplate != null) {
            try {
                redisTemplate.opsForValue().set(key, value, Duration.ofSeconds(timeoutInSeconds));
                log.info("[CACHE] Successfully cached data for key: {} with TTL: {}s", key, timeoutInSeconds);
            } catch (Exception e) {
                log.warn("[CACHE-WARN] Redis unavailable. Skipping cache write for key: {}", key);
            }
        }
    }

    public String getCachedData(String key) {
        if (redisTemplate != null) {
            try {
                String value = redisTemplate.opsForValue().get(key);
                if (value != null) {
                    log.info("[CACHE] Cache hit for key: {}", key);
                    return value;
                }
            } catch (Exception e) {
                log.warn("[CACHE-WARN] Redis unavailable. Falling back to primary data source for key: {}", key);
            }
        }
        log.info("[CACHE] Cache miss for key: {}", key);
        return null;
    }
}
