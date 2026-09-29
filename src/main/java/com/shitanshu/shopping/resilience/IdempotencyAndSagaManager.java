package com.shitanshu.shopping.resilience;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class IdempotencyAndSagaManager {

    // In-memory Idempotency Store (Production mein Redis use hota hai)
    private final Map<String, String> idempotencyStore = new ConcurrentHashMap<>();

    public boolean isDuplicate(String idempotencyKey) {
        return idempotencyStore.containsKey(idempotencyKey);
    }

    public String getExistingResponse(String idempotencyKey) {
        return idempotencyStore.get(idempotencyKey);
    }

    public void saveResponse(String idempotencyKey, String response) {
        idempotencyStore.put(idempotencyKey, response);
    }

    // Distributed SAGA Orchestration Simulation (Compensating Transactions)
    public String executeOrderSaga(String orderId, boolean failInventory) {
        System.out.println("\n--- [SAGA START] Initiating Saga for Order: " + orderId + " ---");

        // Step 1: Create Order
        System.out.println("Saga Step 1: Order Created [PENDING]");

        // Step 2: Deduct Payment
        System.out.println("Saga Step 2: Payment Reserved [SUCCESS]");

        // Step 3: Reserve Inventory
        if (failInventory) {
            System.err.println("Saga Step 3 FAILED: Insufficient stock for Order: " + orderId);
            System.out.println(">>> SAGA COMPENSATION TRIGGERED <<<");
            compensatePayment(orderId);
            compensateOrder(orderId);
            return "SAGA_ABORTED: Inventory failed. Compensating transactions executed (Refund & Order Cancelled).";
        }

        System.out.println("Saga Step 3: Inventory Reserved [SUCCESS]");
        System.out.println("--- [SAGA COMPLETE] Order " + orderId + " fulfilled successfully! ---\n");
        return "SAGA_SUCCESS: Order fulfilled with full distributed consistency.";
    }

    private void compensatePayment(String orderId) {
        System.out.println("Compensation Step: Payment Refunded to User Wallet for Order: " + orderId);
    }

    private void compensateOrder(String orderId) {
        System.out.println("Compensation Step: Order " + orderId + " marked as CANCELLED");
    }
}
