package com.shitanshu.shopping.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class PaymentResilienceService {

    private static final Logger log = LoggerFactory.getLogger(PaymentResilienceService.class);

    private static final String PAYMENT_SERVICE_BREAKER = "paymentService";

    @CircuitBreaker(name = PAYMENT_SERVICE_BREAKER, fallbackMethod = "paymentFallback")
    public String processPayment(Long orderId, Double amount) {
        log.info("[RESILIENCE] Calling payment gateway for Order ID: {} with amount: {}", orderId, amount);
        
        // Simulating external payment gateway call (can throw exception if gateway is down)
        if (amount > 100000.0) {
            throw new RuntimeException("Payment Gateway Timeout / Service Unavailable");
        }
        
        return "SUCCESS - Payment processed for Order " + orderId;
    }

    // Fallback method must match the exact return type and parameters plus a Throwable
    public String paymentFallback(Long orderId, Double amount, Throwable t) {
        log.error("[CIRCUIT-BREAKER-FALLBACK] Payment failed for Order ID: {}. Reason: {}. Executing fallback ledger record.", 
                orderId, t.getMessage());
        return "FALLBACK_PENDING - Payment gateway is currently busy. Order " + orderId + " moved to manual verification queue.";
    }
}
