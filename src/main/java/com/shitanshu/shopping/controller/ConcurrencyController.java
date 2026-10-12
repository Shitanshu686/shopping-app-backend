package com.shitanshu.shopping.controller;

import com.shitanshu.shopping.concurrency.DatabaseConcurrencyService;
import com.shitanshu.shopping.concurrency.IdempotencyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/concurrency")
public class ConcurrencyController {

    private final DatabaseConcurrencyService databaseConcurrencyService;
    private final IdempotencyService idempotencyService;

    public ConcurrencyController(DatabaseConcurrencyService databaseConcurrencyService,
                                 IdempotencyService idempotencyService) {
        this.databaseConcurrencyService = databaseConcurrencyService;
        this.idempotencyService = idempotencyService;
    }

    @PostMapping("/simulate")
    public ResponseEntity<Map<String, Object>> simulateHighConcurrency(
            @RequestParam(defaultValue = "1") Integer productId,
            @RequestParam(defaultValue = "10") int initialStock,
            @RequestParam(defaultValue = "100") int users,
            @RequestParam(defaultValue = "atomic") String strategy) throws InterruptedException {

        Map<String, Object> simulationResult = databaseConcurrencyService.runHighConcurrencyTest(
                productId, initialStock, users, strategy);

        return ResponseEntity.ok(simulationResult);
    }

    @PostMapping("/checkout-idempotent")
    public ResponseEntity<Map<String, Object>> testIdempotentOrder(
            @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey,
            @RequestBody Map<String, Object> orderPayload) {

        if (idempotencyKey == null || idempotencyKey.isBlank()) {
            // Unprotected request path
            String orderId = "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            return ResponseEntity.ok(Map.of(
                    "status", "SUCCESS",
                    "orderId", orderId,
                    "message", "Order placed without idempotency protection",
                    "key", "NONE"
            ));
        }

        String payloadHash = idempotencyService.computePayloadHash(orderPayload);
        IdempotencyService.IdempotencyRecord existingRecord = idempotencyService.lockOrGet(idempotencyKey, payloadHash);

        if (existingRecord != null) {
            // Case 1: Tampered/Mismatched payload with same key
            if (!existingRecord.getPayloadHash().equals(payloadHash)) {
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(Map.of(
                        "status", "IDEMPOTENCY_PAYLOAD_MISMATCH",
                        "error", "Idempotency key reused with mismatched request parameters",
                        "key", idempotencyKey
                ));
            }

            // Case 2: Concurrent identical request still in progress
            if (existingRecord.getStatus() == IdempotencyService.Status.PROCESSING) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                        "status", "CONCURRENT_REQUEST_IN_PROGRESS",
                        "error", "Original checkout request with this key is currently being processed. Please retry shortly.",
                        "key", idempotencyKey
                ));
            }

            // Case 3: Replay completed cached response (Stripe response replay pattern)
            @SuppressWarnings("unchecked") Map<String, Object> replayedResponse = new LinkedHashMap<>((Map<String, Object>) existingRecord.getResponseBody());
            replayedResponse.put("replayed", true);
            return ResponseEntity.status(existingRecord.getHttpStatusCode())
                    .header("Idempotency-Replayed", "true")
                    .body(replayedResponse);
        }

        try {
            // Process checkout business logic once
            String orderId = "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            Map<String, Object> responseData = new LinkedHashMap<>();
            responseData.put("status", "SUCCESS");
            responseData.put("orderId", orderId);
            responseData.put("message", "Order processed and committed successfully");
            responseData.put("key", idempotencyKey);
            responseData.put("replayed", false);

            idempotencyService.completeRequest(idempotencyKey, 200, responseData);

            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            idempotencyService.releaseKey(idempotencyKey);
            throw e;
        }
    }
}
