package com.shitanshu.shopping.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class OrderSagaOrchestrator {

    private static final Logger log = LoggerFactory.getLogger(OrderSagaOrchestrator.class);

    /**
     * Executes the distributed Saga workflow for an order.
     * Steps: 
     * 1. Reserve Inventory (MySQL)
     * 2. Process Payment (MySQL / Gateway)
     * 3. Record Audit Log (PostgreSQL)
     */
    public boolean executeOrderSaga(Long orderId, Double amount) {
        log.info("[SAGA-ORCHESTRATOR] Starting distributed transaction workflow for Order ID: {}", orderId);
        
        try {
            // Step 1: Reserve Inventory simulation
            boolean inventoryReserved = reserveInventoryStep(orderId);
            if (!inventoryReserved) {
                triggerCompensationInventory(orderId);
                return false;
            }

            // Step 2: Process Payment simulation
            boolean paymentProcessed = processPaymentStep(orderId, amount);
            if (!paymentProcessed) {
                // Compensate previous step
                triggerCompensationPayment(orderId);
                triggerCompensationInventory(orderId);
                return false;
            }

            log.info("[SAGA-ORCHESTRATOR] Distributed Saga successfully completed for Order ID: {}", orderId);
            return true;

        } catch (Exception e) {
            log.error("[SAGA-ORCHESTRATOR] Saga failed catastrophically for Order ID: {}. Initiating full rollback.", orderId, e);
            triggerCompensationPayment(orderId);
            triggerCompensationInventory(orderId);
            return false;
        }
    }

    private boolean reserveInventoryStep(Long orderId) {
        log.info("[SAGA-STEP] Reserving inventory for order: {}", orderId);
        // Returns true indicating successful stock lock
        return true; 
    }

    private boolean processPaymentStep(Long orderId, Double amount) {
        log.info("[SAGA-STEP] Charging amount {} for order: {}", amount, orderId);
        // Returns true indicating successful charge
        return true; 
    }

    private void triggerCompensationPayment(Long orderId) {
        log.warn("[SAGA-COMPENSATION] Rolling back payment for failed order: {}", orderId);
    }

    private void triggerCompensationInventory(Long orderId) {
        log.warn("[SAGA-COMPENSATION] Releasing reserved stock for failed order: {}", orderId);
    }
}
