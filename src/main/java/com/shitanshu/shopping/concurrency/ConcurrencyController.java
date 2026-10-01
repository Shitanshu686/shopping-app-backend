package com.shitanshu.shopping.concurrency;

import com.shitanshu.shopping.repository.ProductRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/concurrency")
public class ConcurrencyController {

    private final ConcurrencyDemoService concurrencyService;
    private final DatabaseConcurrencyService dbConcurrencyService;
    private final ProductRepository productRepository;

    public ConcurrencyController(ConcurrencyDemoService concurrencyService,
                                 DatabaseConcurrencyService dbConcurrencyService,
                                 ProductRepository productRepository) {
        this.concurrencyService = concurrencyService;
        this.dbConcurrencyService = dbConcurrencyService;
        this.productRepository = productRepository;
    }

    // Memory-level endpoints
    @GetMapping("/unsafe")
    public ResponseEntity<String> testUnsafe(@RequestParam(defaultValue = "100") int requests) throws InterruptedException {
        return ResponseEntity.ok(concurrencyService.simulateUnsafeDeduction(requests));
    }

    @GetMapping("/atomic")
    public ResponseEntity<String> testAtomic(@RequestParam(defaultValue = "100") int requests) throws InterruptedException {
        return ResponseEntity.ok(concurrencyService.simulateAtomicDeduction(requests));
    }

    @GetMapping("/lock")
    public ResponseEntity<String> testLock(@RequestParam(defaultValue = "100") int requests) throws InterruptedException {
        return ResponseEntity.ok(concurrencyService.simulateReentrantLockDeduction(requests));
    }

    @GetMapping("/async-checkout")
    public CompletableFuture<ResponseEntity<String>> testAsyncCheckout(@RequestParam(defaultValue = "ORD-CONC-1") String orderId) {
        return concurrencyService.processCheckoutParallel(orderId).thenApply(ResponseEntity::ok);
    }

    // ==============================================================
    // DATABASE CONCURRENCY TESTS (Optimistic vs Pessimistic vs Atomic)
    // ==============================================================

    // Test Optimistic Locking (@Version)
    @GetMapping("/db/optimistic")
    public ResponseEntity<String> testDbOptimistic(@RequestParam(defaultValue = "3") Integer productId,
                                                  @RequestParam(defaultValue = "50") int totalThreads) throws InterruptedException {
        dbConcurrencyService.resetProductStock(productId, 20);

        ExecutorService executor = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(totalThreads);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger conflictCount = new AtomicInteger(0);

        for (int i = 0; i < totalThreads; i++) {
            executor.submit(() -> {
                try {
                    boolean success = dbConcurrencyService.deductWithOptimisticLock(productId, 1);
                    if (success) successCount.incrementAndGet();
                } catch (ObjectOptimisticLockingFailureException e) {
                    conflictCount.incrementAndGet(); // Version mismatch detected
                } catch (Exception ignored) {
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(10, TimeUnit.SECONDS);
        executor.shutdown();

        int finalStock = productRepository.findById(productId).map(p -> p.getStock()).orElse(-1);
        return ResponseEntity.ok("Optimistic Locking Test -> Successful Deductions: " + successCount.get() +
                ", Version Conflict Collisions: " + conflictCount.get() +
                ", Final DB Stock: " + finalStock);
    }

    // Test Pessimistic Locking (SELECT ... FOR UPDATE)
    @GetMapping("/db/pessimistic")
    public ResponseEntity<String> testDbPessimistic(@RequestParam(defaultValue = "3") Integer productId,
                                                   @RequestParam(defaultValue = "50") int totalThreads) throws InterruptedException {
        dbConcurrencyService.resetProductStock(productId, 20);

        ExecutorService executor = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(totalThreads);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger outOfStockCount = new AtomicInteger(0);

        for (int i = 0; i < totalThreads; i++) {
            executor.submit(() -> {
                try {
                    boolean success = dbConcurrencyService.deductWithPessimisticLock(productId, 1);
                    if (success) {
                        successCount.incrementAndGet();
                    } else {
                        outOfStockCount.incrementAndGet();
                    }
                } catch (Exception ignored) {
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(10, TimeUnit.SECONDS);
        executor.shutdown();

        int finalStock = productRepository.findById(productId).map(p -> p.getStock()).orElse(-1);
        return ResponseEntity.ok("Pessimistic Locking Test (Row Lock) -> Successful Deductions: " + successCount.get() +
                ", Rejected (Out of Stock): " + outOfStockCount.get() +
                ", Final DB Stock: " + finalStock);
    }
}
