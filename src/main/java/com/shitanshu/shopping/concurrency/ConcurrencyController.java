package com.shitanshu.shopping.concurrency;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/concurrency")
public class ConcurrencyController {

    @Autowired
    private ConcurrencyDemoService demoService;

    @Autowired
    private DatabaseConcurrencyService dbService;

    // 1. Race condition (Unsafe)
    @GetMapping("/unsafe")
    public ResponseEntity<String> testUnsafe() throws InterruptedException {
        return ResponseEntity.ok(demoService.simulateUnsafeDeduction(50));
    }

    // 2. Atomic Variables (Lock-free)
    @GetMapping("/atomic")
    public ResponseEntity<String> testAtomic() throws InterruptedException {
        return ResponseEntity.ok(demoService.simulateAtomicDeduction(50));
    }

    // 3. ReentrantLock
    @GetMapping("/lock")
    public ResponseEntity<String> testLock() throws InterruptedException {
        return ResponseEntity.ok(demoService.simulateReentrantLockDeduction(50));
    }

    // 4. Intrinsic Synchronization (synchronized block)
    @GetMapping("/synchronized")
    public ResponseEntity<String> testSynchronized() throws InterruptedException {
        return ResponseEntity.ok(demoService.simulateSynchronizedDeduction(50));
    }

    // 5. Deadlock Detection & Prevention (tryLock timeout)
    @GetMapping("/deadlock")
    public ResponseEntity<String> testDeadlock() throws Exception {
        return ResponseEntity.ok(demoService.simulateDeadlockResolution());
    }

    // 6. Starvation vs Fair Locks
    @GetMapping("/starvation")
    public ResponseEntity<String> testStarvation() throws InterruptedException {
        return ResponseEntity.ok(demoService.simulateStarvationVsFairLock());
    }

    // 7. Concurrent Collections (ConcurrentHashMap vs HashMap)
    @GetMapping("/collections")
    public ResponseEntity<String> testCollections() throws InterruptedException {
        return ResponseEntity.ok(demoService.simulateConcurrentCollections());
    }

    // 8. CompletableFuture Async Checkout Pipeline
    @GetMapping("/async-checkout")
    public CompletableFuture<ResponseEntity<String>> testAsyncCheckout() {
        return demoService.processCheckoutParallel("ORD-CONC-1")
                .thenApply(ResponseEntity::ok);
    }

    // 9. Database Optimistic Locking
    @GetMapping("/db/optimistic")
    public ResponseEntity<String> testDbOptimistic(
            @RequestParam(defaultValue = "3") Integer productId,
            @RequestParam(defaultValue = "50") int totalThreads) throws InterruptedException {

        ExecutorService executor = Executors.newFixedThreadPool(totalThreads);
        CountDownLatch latch = new CountDownLatch(totalThreads);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger collisionCount = new AtomicInteger(0);

        for (int i = 0; i < totalThreads; i++) {
            executor.submit(() -> {
                try {
                    boolean success = dbService.deductWithOptimisticLock(productId, 1);
                    if (success) {
                        successCount.incrementAndGet();
                    }
                } catch (ObjectOptimisticLockingFailureException e) {
                    collisionCount.incrementAndGet();
                } catch (Exception ignored) {
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(10, TimeUnit.SECONDS);
        executor.shutdown();

        return ResponseEntity.ok("Optimistic Locking Test -> Successful Deductions: " + successCount.get() +
                ", Version Conflict Collisions: " + collisionCount.get() +
                ", Final DB Stock: " + (20 - successCount.get()));
    }

    // 10. Database Pessimistic Locking
    @GetMapping("/db/pessimistic")
    public ResponseEntity<String> testDbPessimistic(
            @RequestParam(defaultValue = "3") Integer productId,
            @RequestParam(defaultValue = "50") int totalThreads) throws InterruptedException {

        ExecutorService executor = Executors.newFixedThreadPool(totalThreads);
        CountDownLatch latch = new CountDownLatch(totalThreads);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger rejectCount = new AtomicInteger(0);

        for (int i = 0; i < totalThreads; i++) {
            executor.submit(() -> {
                try {
                    boolean success = dbService.deductWithPessimisticLock(productId, 1);
                    if (success) {
                        successCount.incrementAndGet();
                    } else {
                        rejectCount.incrementAndGet();
                    }
                } catch (Exception e) {
                    rejectCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(10, TimeUnit.SECONDS);
        executor.shutdown();

        return ResponseEntity.ok("Pessimistic Locking Test (Row Lock) -> Successful Deductions: " + successCount.get() +
                ", Rejected (Out of Stock): " + rejectCount.get() +
                ", Final DB Stock: " + (20 - successCount.get()));
    }
}
