package com.shitanshu.shopping.controller;

import com.shitanshu.shopping.concurrency.ConcurrencyDemoService;
import com.shitanshu.shopping.concurrency.DatabaseConcurrencyService;
import com.shitanshu.shopping.concurrency.IdempotencyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/concurrency")
@CrossOrigin(origins = "*")
public class ConcurrencyController {

    private final DatabaseConcurrencyService databaseConcurrencyService;
    private final IdempotencyService idempotencyService;

    public ConcurrencyController(DatabaseConcurrencyService databaseConcurrencyService,
                                 IdempotencyService idempotencyService) {
        this.databaseConcurrencyService = databaseConcurrencyService;
        this.idempotencyService = idempotencyService;
    }

    // 1. Run 100-user simulation (strategy: unsafe | pessimistic | atomic)
    @PostMapping("/simulate")
    public ResponseEntity<Map<String, Object>> simulateHighConcurrency(
            @RequestParam(defaultValue = "1") Integer productId,
            @RequestParam(defaultValue = "10") int initialStock,
            @RequestParam(defaultValue = "100") int users,
            @RequestParam(defaultValue = "atomic") String strategy) throws InterruptedException {

        Map<String, Object> result = databaseConcurrencyService.runHighConcurrencyTest(
                productId, initialStock, users, strategy);

        return ResponseEntity.ok(result);
    }

    // 2. Idempotency Check Demo
    @PostMapping("/checkout-idempotent")
    public ResponseEntity<Map<String, Object>> testIdempotentOrder(
            @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey,
            @RequestBody Map<String, Object> orderPayload) {

        if (idempotencyKey != null && !idempotencyKey.isBlank()) {
            if (idempotencyService.isDuplicate(idempotencyKey)) {
                return ResponseEntity.status(409).body(Map.of(
                        "status", "DUPLICATE_REQUEST_BLOCKED",
                        "message", "Order with key " + idempotencyKey + " already processed! Duplicate placement prevented."
                ));
            }
            idempotencyService.saveKey(idempotencyKey, orderPayload);
        }

        return ResponseEntity.ok(Map.of(
            "status", "SUCCESS",
            "message", "Order processed successfully",
            "key", idempotencyKey != null ? idempotencyKey : "NONE"
        ));
    }
}
