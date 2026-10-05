package com.shitanshu.shopping.concurrency;

import org.springframework.stereotype.Service;

import java.util.*;
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

    // 4. Synchronized block state
    private int synchronizedStock = 50;
    private final Object monitorLock = new Object();

    // 5. Deadlock test locks
    private final Object lockA = new Object();
    private final Object lockB = new Object();
    private final ReentrantLock tryLockA = new ReentrantLock();
    private final ReentrantLock tryLockB = new ReentrantLock();

    // Reset counts for repeated testing
    public void resetStocks(int initialStock) {
        this.unsafeStock = initialStock;
        this.atomicStock.set(initialStock);
        this.lockedStock = initialStock;
        this.synchronizedStock = initialStock;
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
                    if (stockLock.tryLock(500, TimeUnit.MILLISECONDS)) {
                        try {
                            if (lockedStock > 0) {
                                Thread.sleep(2);
                                lockedStock--;
                                successfulOrders.incrementAndGet();
                            }
                        } finally {
                            stockLock.unlock();
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
        CompletableFuture<String> paymentFuture = CompletableFuture.supplyAsync(() -> {
            simulateLatency(150);
            return "Payment-Verified";
        });

        CompletableFuture<String> inventoryFuture = CompletableFuture.supplyAsync(() -> {
            simulateLatency(120);
            return "Inventory-Reserved";
        });

        CompletableFuture<String> invoiceFuture = CompletableFuture.supplyAsync(() -> {
            simulateLatency(180);
            return "Invoice-Generated";
        });

        return CompletableFuture.allOf(paymentFuture, inventoryFuture, invoiceFuture)
                .thenApply(v -> {
                    String pay = paymentFuture.join();
                    String inv = inventoryFuture.join();
                    String bill = invoiceFuture.join();
                    return "Order " + orderId + " completed in parallel: [" + pay + ", " + inv + ", " + bill + "]";
                });
    }

    // --- TEST E: Synchronized Block (Java Intrinsic Monitor Lock) ---
    public String simulateSynchronizedDeduction(int totalRequests) throws InterruptedException {
        resetStocks(50);
        ExecutorService executor = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(totalRequests);
        AtomicInteger successfulOrders = new AtomicInteger(0);

        for (int i = 0; i < totalRequests; i++) {
            executor.submit(() -> {
                try {
                    synchronized (monitorLock) {
                        if (synchronizedStock > 0) {
                            Thread.sleep(2);
                            synchronizedStock--;
                            successfulOrders.incrementAndGet();
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

        return "Synchronized Result: Remaining stock: " + synchronizedStock +
               ", Successful orders: " + successfulOrders.get() + " (Intrinsic monitor locked)";
    }

    // --- TEST F: Deadlock Simulation & Prevention ---
    public String simulateDeadlockResolution() throws InterruptedException, ExecutionException, TimeoutException {
        // Safe lock acquisition with timeouts to prevent permanent freeze
        ExecutorService executor = Executors.newFixedThreadPool(2);

        Future<String> worker1 = executor.submit(() -> {
            try {
                if (tryLockA.tryLock(200, TimeUnit.MILLISECONDS)) {
                    try {
                        Thread.sleep(50); // Holding Lock A, requesting Lock B
                        if (tryLockB.tryLock(200, TimeUnit.MILLISECONDS)) {
                            try {
                                return "Worker-1 acquired LockA and LockB successfully";
                            } finally {
                                tryLockB.unlock();
                            }
                        } else {
                            return "Worker-1: LockB timeout - Deadlock avoided via tryLock timeout";
                        }
                    } finally {
                        tryLockA.unlock();
                    }
                }
                return "Worker-1: LockA acquisition failed";
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return "Worker-1 interrupted";
            }
        });

        Future<String> worker2 = executor.submit(() -> {
            try {
                if (tryLockB.tryLock(200, TimeUnit.MILLISECONDS)) {
                    try {
                        Thread.sleep(50); // Holding Lock B, requesting Lock A
                        if (tryLockA.tryLock(200, TimeUnit.MILLISECONDS)) {
                            try {
                                return "Worker-2 acquired LockB and LockA successfully";
                            } finally {
                                tryLockA.unlock();
                            }
                        } else {
                            return "Worker-2: LockA timeout - Deadlock avoided via tryLock timeout";
                        }
                    } finally {
                        tryLockB.unlock();
                    }
                }
                return "Worker-2: LockB acquisition failed";
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return "Worker-2 interrupted";
            }
        });

        String res1 = worker1.get(2, TimeUnit.SECONDS);
        String res2 = worker2.get(2, TimeUnit.SECONDS);
        executor.shutdown();

        return "Deadlock Simulation Result -> [" + res1 + " | " + res2 + "]";
    }

    // --- TEST G: Starvation vs Fair Lock ---
    public String simulateStarvationVsFairLock() throws InterruptedException {
        ReentrantLock unfairLock = new ReentrantLock(false); // Unfair (prone to starvation)
        ReentrantLock fairLock = new ReentrantLock(true);    // Fair (FIFO queue)

        AtomicInteger unfairHighPriorityCount = new AtomicInteger(0);
        AtomicInteger unfairLowPriorityCount = new AtomicInteger(0);

        AtomicInteger fairHighPriorityCount = new AtomicInteger(0);
        AtomicInteger fairLowPriorityCount = new AtomicInteger(0);

        // Run Unfair Lock Test
        testLockFairness(unfairLock, unfairHighPriorityCount, unfairLowPriorityCount);

        // Run Fair Lock Test
        testLockFairness(fairLock, fairHighPriorityCount, fairLowPriorityCount);

        return "Starvation vs Fair Lock -> " +
               "Unfair Lock [High: " + unfairHighPriorityCount.get() + ", Low: " + unfairLowPriorityCount.get() + "] vs " +
               "Fair Lock [High: " + fairHighPriorityCount.get() + ", Low: " + fairLowPriorityCount.get() + "]";
    }

    private void testLockFairness(ReentrantLock lock, AtomicInteger highCount, AtomicInteger lowCount) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(4);
        CountDownLatch latch = new CountDownLatch(4);

        // 3 aggressive workers
        for (int i = 0; i < 3; i++) {
            executor.submit(() -> {
                for (int j = 0; j < 30; j++) {
                    lock.lock();
                    try {
                        highCount.incrementAndGet();
                    } finally {
                        lock.unlock();
                    }
                }
                latch.countDown();
            });
        }

        // 1 normal worker that can starve under unfair conditions
        executor.submit(() -> {
            for (int j = 0; j < 30; j++) {
                lock.lock();
                try {
                    lowCount.incrementAndGet();
                } finally {
                    lock.unlock();
                }
            }
            latch.countDown();
        });

        latch.await(3, TimeUnit.SECONDS);
        executor.shutdown();
    }

    // --- TEST H: Concurrent Collections (ConcurrentHashMap vs HashMap) ---
    public String simulateConcurrentCollections() throws InterruptedException {
        int threads = 10;
        int operationsPerThread = 500;

        // Thread-Safe ConcurrentHashMap
        ConcurrentHashMap<String, Integer> concurrentMap = new ConcurrentHashMap<>();
        // Non-Thread-Safe HashMap
        Map<String, Integer> standardMap = new HashMap<>();

        ExecutorService executor = Executors.newFixedThreadPool(threads);
        CountDownLatch latch = new CountDownLatch(threads);

        for (int i = 0; i < threads; i++) {
            final int workerId = i;
            executor.submit(() -> {
                try {
                    for (int j = 0; j < operationsPerThread; j++) {
                        // ConcurrentHashMap atomic increment
                        concurrentMap.compute("stock-key", (k, v) -> (v == null) ? 1 : v + 1);

                        // HashMap non-thread-safe update (prone to silent data loss)
                        try {
                            Integer current = standardMap.get("stock-key");
                            standardMap.put("stock-key", (current == null) ? 1 : current + 1);
                        } catch (Exception ignored) {}
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(5, TimeUnit.SECONDS);
        executor.shutdown();

        int expected = threads * operationsPerThread;
        int chmActual = concurrentMap.getOrDefault("stock-key", 0);
        int hmActual = standardMap.getOrDefault("stock-key", 0);

        return "Collections Result -> Expected updates: " + expected +
               " | ConcurrentHashMap: " + chmActual + " (Thread-Safe, Exact)" +
               " | Standard HashMap: " + hmActual + " (Lost Updates / Data Corruption)";
    }

    private void simulateLatency(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
