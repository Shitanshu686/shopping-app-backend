package com.shitanshu.shopping.resilience;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/resilience")
public class ResilienceTestController {

    private final ResilientPaymentService paymentService;
    private final IdempotencyAndSagaManager sagaManager;

    public ResilienceTestController(ResilientPaymentService paymentService, IdempotencyAndSagaManager sagaManager) {
        this.paymentService = paymentService;
        this.sagaManager = sagaManager;
    }

    // Test 1: Circuit Breaker, Retry, Bulkhead, Fallback
    @GetMapping("/payment")
    public ResponseEntity<String> testPayment(
            @RequestParam(defaultValue = "ORD-101") String orderId,
            @RequestParam(defaultValue = "false") boolean fail,
            @RequestParam(defaultValue = "false") boolean timeout) {
        String result = paymentService.processPayment(orderId, fail, timeout);
        return ResponseEntity.ok(result);
    }

    // Test 2: Idempotency Pattern via Header
    @PostMapping("/idempotent-order")
    public ResponseEntity<String> testIdempotency(
            @RequestHeader("Idempotency-Key") String idempotencyKey,
            @RequestParam String orderId) {

        if (sagaManager.isDuplicate(idempotencyKey)) {
            return ResponseEntity.ok("[CACHED/IDEMPOTENT RESULT] " + sagaManager.getExistingResponse(idempotencyKey));
        }

        String freshResult = "ORDER_PLACED_FOR_" + orderId + "_TIMESTAMP_" + System.currentTimeMillis();
        sagaManager.saveResponse(idempotencyKey, freshResult);
        return ResponseEntity.ok("[NEW TRANSACTION EXECUTED] " + freshResult);
    }

    // Test 3: Saga Pattern with Compensation
    @PostMapping("/saga")
    public ResponseEntity<String> testSaga(
            @RequestParam(defaultValue = "ORD-999") String orderId,
            @RequestParam(defaultValue = "false") boolean failInventory) {
        String outcome = sagaManager.executeOrderSaga(orderId, failInventory);
        return ResponseEntity.ok(outcome);
    }
}
