package com.shitanshu.shopping.concurrency;

import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class IdempotencyService {

    public enum Status {
        PROCESSING,
        COMPLETED
    }

    public static class IdempotencyRecord {
        private final String payloadHash;
        private volatile Status status;
        private volatile Map<String, Object> responseBody;
        private volatile int httpStatusCode;
        private final long createdAt;

        public IdempotencyRecord(String payloadHash) {
            this.payloadHash = payloadHash;
            this.status = Status.PROCESSING;
            this.createdAt = System.currentTimeMillis();
            this.httpStatusCode = 200;
        }

        public String getPayloadHash() { return payloadHash; }
        public Status getStatus() { return status; }
        public Map<String, Object> getResponseBody() { return responseBody; }
        public int getHttpStatusCode() { return httpStatusCode; }
        public long getCreatedAt() { return createdAt; }

        public synchronized void complete(int httpStatusCode, Map<String, Object> responseBody) {
            this.status = Status.COMPLETED;
            this.httpStatusCode = httpStatusCode;
            this.responseBody = responseBody;
        }
    }

    private final ConcurrentHashMap<String, IdempotencyRecord> store = new ConcurrentHashMap<>();
    private static final long EXPIRATION_TIME_MS = TimeUnit.MINUTES.toMillis(15);

    public String computePayloadHash(Map<String, Object> payload) {
        if (payload == null) return "";
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(payload.toString().getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            return String.valueOf(payload.hashCode());
        }
    }

    /**
     * Atomically locks key in PROCESSING state if absent.
     * Returns null if key was newly reserved, or existing IdempotencyRecord if already present.
     */
    public IdempotencyRecord lockOrGet(String key, String payloadHash) {
        cleanupExpiredKeys();
        IdempotencyRecord newRecord = new IdempotencyRecord(payloadHash);
        return store.putIfAbsent(key, newRecord);
    }

    public void completeRequest(String key, int httpStatusCode, Map<String, Object> responseBody) {
        IdempotencyRecord record = store.get(key);
        if (record != null) {
            record.complete(httpStatusCode, responseBody);
        }
    }

    public void releaseKey(String key) {
        store.remove(key);
    }

    private void cleanupExpiredKeys() {
        long now = System.currentTimeMillis();
        store.entrySet().removeIf(entry -> (now - entry.getValue().getCreatedAt()) > EXPIRATION_TIME_MS);
    }
}
