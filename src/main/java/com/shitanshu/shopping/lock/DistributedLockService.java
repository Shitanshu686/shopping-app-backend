package com.shitanshu.shopping.lock;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class DistributedLockService {

    private static final Logger log = LoggerFactory.getLogger(DistributedLockService.class);
    private static final String LOCK_PREFIX = "lock:";

    @Autowired(required = false)
    private StringRedisTemplate redisTemplate;

    public boolean acquireLock(String lockKey, String lockValue, long timeoutInSeconds) {
        if (redisTemplate == null) {
            log.warn("[LOCK-WARN] Redis unavailable. Bypassing distributed lock for key: {}", lockKey);
            return true; // Fallback for standalone/no-redis environments
        }

        try {
            String fullKey = LOCK_PREFIX + lockKey;
            Boolean success = redisTemplate.opsForValue()
                    .setIfAbsent(fullKey, lockValue, Duration.ofSeconds(timeoutInSeconds));
            
            if (Boolean.TRUE.equals(success)) {
                log.info("[LOCK] Successfully acquired lock for key: {}", lockKey);
                return true;
            }
        } catch (Exception e) {
            log.error("[LOCK-ERR] Error acquiring lock for key: {}. Error: {}", lockKey, e.getMessage());
        }
        
        log.warn("[LOCK] Failed to acquire lock for key: {} (Already locked)", lockKey);
        return false;
    }

    public void releaseLock(String lockKey, String lockValue) {
        if (redisTemplate == null) {
            return;
        }

        try {
            String fullKey = LOCK_PREFIX + lockKey;
            String currentValue = redisTemplate.opsForValue().get(fullKey);
            
            if (lockValue.equals(currentValue)) {
                redisTemplate.delete(fullKey);
                log.info("[LOCK] Successfully released lock for key: {}", lockKey);
            }
        } catch (Exception e) {
            log.error("[LOCK-ERR] Error releasing lock for key: {}. Error: {}", lockKey, e.getMessage());
        }
    }
}
