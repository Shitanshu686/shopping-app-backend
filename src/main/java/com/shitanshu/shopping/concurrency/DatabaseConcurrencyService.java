package com.shitanshu.shopping.concurrency;

import com.shitanshu.shopping.model.Product;
import com.shitanshu.shopping.repository.ProductRepository;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class DatabaseConcurrencyService {

    private final ProductRepository productRepository;

    public DatabaseConcurrencyService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // 1. OPTIMISTIC LOCKING: Uses JPA @Version. Fails fast if concurrent modification occurs.
    @Transactional
    public boolean deductWithOptimisticLock(Integer productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));

        if (product.getStock() < quantity) {
            return false;
        }

        product.setStock(product.getStock() - quantity);
        productRepository.save(product); // Hibernate checks version matching before commit
        return true;
    }

    // 2. PESSIMISTIC LOCKING: Acquires database row-level lock (SELECT ... FOR UPDATE).
    @Transactional
    public boolean deductWithPessimisticLock(Integer productId, int quantity) {
        Product product = productRepository.findByIdWithPessimisticLock(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));

        if (product.getStock() < quantity) {
            return false;
        }

        product.setStock(product.getStock() - quantity);
        productRepository.save(product);
        return true;
    }

    // 3. ATOMIC SQL UPDATE: Single atomic statement at database level.
    @Transactional
    public boolean deductWithAtomicQuery(Integer productId, int quantity) {
        int rowsUpdated = productRepository.decrementStockAtomic(productId, quantity);
        return rowsUpdated > 0;
    }

    // Helper: Reset stock for testing
    @Transactional
    public void resetProductStock(Integer productId, int stock) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setStock(stock);
        productRepository.save(product);
    }
}
