package com.shitanshu.shopping.concurrency;

import org.springframework.stereotype.Service;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class ConcurrencyDemoService {

    // 1. Thread-Unsafe state (Race Condition dekhne ke liye)
    private int unsafeStock = 50;

    // 2. Thread-Safe via AtomicInteger (Lock-free hardware CAS operations)
    private final AtomicInteger atomicStock = new AtomicInteger(50);

    // 3. Thread-Safe via Explicit ReentrantLock (Fine-grained control)
    private int lockedStock = 50;
    private final ReentrantLock stockLock = new ReentrantLock(true); // Fair Lock

    // Reset counts for repeated testing
    public void resetStocks(int initialStock) {
        this.unsafeStock = initialStock;
        this.atomicStock.set(initialStock);
        this.lockedStock = initialStock;
    }

    // --- TEST A: Unsafe Deduction (Simulates severe race condition) ---
    public String simulateUnsafeDeduction(int totalRequests) throws InterruptedException {
        resetStocks(50);
        ExecutorService executor = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(totalRequests);

        for (int i = 0; i < totalRequests; i++) {
            executor.submit(() -> {
                try {
                    if (unsafeStock > 0) {
                        // Artificial delay to widen the race condition window
                        Thread.sleep(5);
                        unsafeStock--;
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(5, TimeUnit.SECONDS);
        executor.shutdown();

        return "Unsafe Result: Expected stock <= 0, actual remaining: " + unsafeStock + 
               " (Race conditions cause overselling or missed updates)";
    }

    // --- TEST B: AtomicInteger Deduction (Thread-safe, non-blocking) ---
    public String simulateAtomicDeduction(int totalRequests) throws InterruptedException {
        resetStocks(50);
        ExecutorService executor = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(totalRequests);
        AtomicInteger successfulOrders = new AtomicInteger(0);

        for (int i = 0; i < totalRequests; i++) {
            executor.submit(() -> {
                try {
                    // Loop until CAS succeeds or stock is exhausted
                    while (true) {
                        int current = atomicStock.get();
                        if (current <= 0) break;
                        if (atomicStock.compareAndSet(current, current - 1)) {
                            successfulOrders.incrementAndGet();
                            break;
                        }
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(5, TimeUnit.SECONDS);
        executor.shutdown();

        return "Atomic Result: Remaining stock: " + atomicStock.get() + 
               ", Successful orders: " + successfulOrders.get() + " (Zero overselling guaranteed)";
    }

    // --- TEST C: ReentrantLock Deduction (Fair thread acquisition) ---
    public String simulateReentrantLockDeduction(int totalRequests) throws InterruptedException {
        resetStocks(50);
        ExecutorService executor = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(totalRequests);
        AtomicInteger successfulOrders = new AtomicInteger(0);

        for (int i = 0; i < totalRequests; i++) {
            executor.submit(() -> {
                try {
                    // Try to acquire lock with 500ms timeout (prevents starvation/deadlock)
                    if (stockLock.tryLock(500, TimeUnit.MILLISECONDS)) {
                        try {
                            if (lockedStock > 0) {
                                Thread.sleep(2);
                                lockedStock--;
                                successfulOrders.incrementAndGet();
                            }
                        } finally {
                            stockLock.unlock(); // Always release in finally block
                        }
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(5, TimeUnit.SECONDS);
        executor.shutdown();

        return "ReentrantLock Result: Remaining stock: " + lockedStock + 
               ", Successful orders: " + successfulOrders.get();
    }

    // --- TEST D: Async Parallel Processing via CompletableFuture ---
    public CompletableFuture<String> processCheckoutParallel(String orderId) {
        // Task 1: Verify Payment
        CompletableFuture<String> paymentFuture = CompletableFuture.supplyAsync(() -> {
            simulateLatency(150);
            return "Payment-Verified";
        });

        // Task 2: Reserve Inventory
        CompletableFuture<String> inventoryFuture = CompletableFuture.supplyAsync(() -> {
            simulateLatency(120);
            return "Inventory-Reserved";
        });

        // Task 3: Generate Invoice
        CompletableFuture<String> invoiceFuture = CompletableFuture.supplyAsync(() -> {
            simulateLatency(180);
            return "Invoice-Generated";
        });

        // Combine all 3 non-blocking tasks asynchronously
        return CompletableFuture.allOf(paymentFuture, inventoryFuture, invoiceFuture)
                .thenApply(v -> {
                    String pay = paymentFuture.join();
                    String inv = inventoryFuture.join();
                    String bill = invoiceFuture.join();
                    return "Order " + orderId + " completed in parallel: [" + pay + ", " + inv + ", " + bill + "]";
                });
    }

    private void simulateLatency(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
