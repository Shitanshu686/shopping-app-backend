package com.shitanshu.shopping.resilience;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ResilientPaymentService {

    private final AtomicInteger attemptCount = new AtomicInteger(0);

    // Resilience Stack: Circuit Breaker -> Retry -> Bulkhead -> Fallback
    @CircuitBreaker(name = "paymentService", fallbackMethod = "paymentFallback")
    @Retry(name = "paymentService")
    @Bulkhead(name = "paymentService")
    public String processPayment(String orderId, boolean simulateFailure, boolean simulateTimeout) {
        int attempt = attemptCount.incrementAndGet();
        System.out.println("===> [PaymentService] Attempt #" + attempt + " for Order: " + orderId);

        if (simulateTimeout) {
            try {
                // Simulating network latency / timeout breach
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        if (simulateFailure) {
            throw new RuntimeException("Downstream Payment Gateway Unreachable (503 Service Unavailable)!");
        }

        return "PAYMENT_SUCCESS: Order " + orderId + " processed successfully on attempt #" + attempt;
    }

    // Fallback Method triggered when retries exhaust, circuit opens, or bulkhead rejects
    public String paymentFallback(String orderId, boolean simulateFailure, boolean simulateTimeout, Throwable ex) {
        System.err.println("===> [FALLBACK TRIGGERED] Reason: " + ex.getClass().getSimpleName() + " - " + ex.getMessage());
        return "FALLBACK_RESPONSE: Payment for Order " + orderId + " queued into offline reconciliation queue. Reason: " + ex.getMessage();
    }
}
