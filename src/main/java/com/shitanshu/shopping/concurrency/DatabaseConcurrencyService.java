package com.shitanshu.shopping.concurrency;

import com.shitanshu.shopping.model.Product;
import com.shitanshu.shopping.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class DatabaseConcurrencyService {

    private final ProductRepository productRepository;

    public DatabaseConcurrencyService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public void resetProductStock(Integer productId, int stock) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found: " + productId));
        product.setStock(stock);
        productRepository.save(product);
    }

    @Transactional(readOnly = true)
    public int getProductStock(Integer productId) {
        return productRepository.findById(productId)
                .map(Product::getStock)
                .orElse(0);
    }

    // 1. UNSAFE DEDUCTION (Race condition prone - Read then Write without Lock)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean deductUnsafe(Integer productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getStock() >= quantity) {
            try {
                Thread.sleep(5);
            } catch (InterruptedException ignored) {}
            product.setStock(product.getStock() - quantity);
            productRepository.save(product);
            return true;
        }
        return false;
    }

    // 2. OPTIMISTIC LOCKING (JPA @Version based)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean deductWithOptimisticLock(Integer productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found: " + productId));

        if (product.getStock() < quantity) {
            return false;
        }

        product.setStock(product.getStock() - quantity);
        productRepository.save(product);
        return true;
    }

    // 3. PESSIMISTIC LOCK DEDUCTION (Row lock - SELECT ... FOR UPDATE)
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
    public boolean deductWithPessimisticLock(Integer productId, int quantity) {
        Product product = productRepository.findByIdWithPessimisticLock(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getStock() >= quantity) {
            product.setStock(product.getStock() - quantity);
            productRepository.save(product);
            return true;
        }
        return false;
    }

    // 4. ATOMIC SQL UPDATE DEDUCTION (Single query - No lock wait)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean deductWithAtomicQuery(Integer productId, int quantity) {
        int updatedRows = productRepository.decrementStockAtomic(productId, quantity);
        return updatedRows > 0;
    }

    // --- HIGH-CONCURRENCY SIMULATION RUNNER (100 Users simultaneously) ---
    public Map<String, Object> runHighConcurrencyTest(Integer productId, int initialStock, int totalUsers, String strategy) throws InterruptedException {
        resetProductStock(productId, initialStock);

        ExecutorService executor = Executors.newFixedThreadPool(25);
        CountDownLatch startSignal = new CountDownLatch(1);
        CountDownLatch doneSignal = new CountDownLatch(totalUsers);

        AtomicInteger successfulOrders = new AtomicInteger(0);
        AtomicInteger failedOrders = new AtomicInteger(0);

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < totalUsers; i++) {
            executor.submit(() -> {
                try {
                    startSignal.await();
                    boolean success = false;
                    if ("unsafe".equalsIgnoreCase(strategy)) {
                        success = deductUnsafe(productId, 1);
                    } else if ("optimistic".equalsIgnoreCase(strategy)) {
                        success = deductWithOptimisticLock(productId, 1);
                    } else if ("pessimistic".equalsIgnoreCase(strategy)) {
                        success = deductWithPessimisticLock(productId, 1);
                    } else if ("atomic".equalsIgnoreCase(strategy)) {
                        success = deductWithAtomicQuery(productId, 1);
                    }

                    if (success) {
                        successfulOrders.incrementAndGet();
                    } else {
                        failedOrders.incrementAndGet();
                    }
                } catch (Exception e) {
                    failedOrders.incrementAndGet();
                } finally {
                    doneSignal.countDown();
                }
            });
        }

        startSignal.countDown();
        doneSignal.await(15, TimeUnit.SECONDS);
        executor.shutdown();

        long duration = System.currentTimeMillis() - startTime;
        int finalStock = getProductStock(productId);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("strategy", strategy.toUpperCase());
        result.put("initialStock", initialStock);
        result.put("totalConcurrentUsers", totalUsers);
        result.put("successfulOrders", successfulOrders.get());
        result.put("failedOrders", failedOrders.get());
        result.put("finalStockInDB", finalStock);
        result.put("oversold", (successfulOrders.get() > initialStock));
        result.put("executionTimeMs", duration);

        return result;
    }
}
