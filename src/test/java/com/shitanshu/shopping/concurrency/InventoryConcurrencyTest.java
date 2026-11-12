package com.shitanshu.shopping.concurrency;

import com.shitanshu.shopping.model.Product;
import com.shitanshu.shopping.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryConcurrencyTest {

    @Mock
    private ProductRepository productRepository;

    @Test
    void concurrentProductSave_ShouldHandleMultiThreading() throws InterruptedException {
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        int numberOfThreads = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        AtomicInteger successCount = new AtomicInteger(0);

        for (int i = 0; i < numberOfThreads; i++) {
            final int index = i;
            executorService.submit(() -> {
                try {
                    Product product = new Product();
                    product.setName("Product " + index);
                    product.setPrice(100.0 * index);
                    product.setStock(50);
                    productRepository.save(product);
                    successCount.incrementAndGet();
                } catch (Exception ignored) {
                }
            });
        }

        executorService.shutdown();
        boolean finished = executorService.awaitTermination(10, TimeUnit.SECONDS);
        assertTrue(finished, "Threads did not terminate in time");
        assertEquals(numberOfThreads, successCount.get());
        verify(productRepository, times(numberOfThreads)).save(any(Product.class));
    }
}
