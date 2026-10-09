package com.shitanshu.shopping.concurrency;

import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class IdempotencyService {

    private static class CacheEntry {
        final Object response;
        final long timestamp;

        CacheEntry(Object response) {
            this.response = response;
            this.timestamp = System.currentTimeMillis();
        }
    }

    // In-memory thread-safe map for idempotent request keys
    private final ConcurrentHashMap<String, CacheEntry> idempotencyStore = new ConcurrentHashMap<>();
    private static final long EXPIRATION_TIME_MS = TimeUnit.MINUTES.toMillis(10);

    public boolean isDuplicate(String key) {
        cleanupExpiredKeys();
        return idempotencyStore.containsKey(key);
    }

    public Object getResponse(String key) {
        CacheEntry entry = idempotencyStore.get(key);
        return entry != null ? entry.response : null;
    }

    public void saveKey(String key, Object response) {
        idempotencyStore.put(key, new CacheEntry(response));
    }

    private void cleanupExpiredKeys() {
        long now = System.currentTimeMillis();
        idempotencyStore.entrySet().removeIf(entry -> (now - entry.getValue().timestamp) > EXPIRATION_TIME_MS);
    }
}
