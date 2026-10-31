package com.shitanshu.shopping.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

@Service
public class BusinessMetricsService {

    private final Counter orderSuccessCounter;
    private final Counter orderFailureCounter;
    private final Counter cacheHitCounter;
    private final Counter cacheMissCounter;

    public BusinessMetricsService(MeterRegistry meterRegistry) {
        this.orderSuccessCounter = Counter.builder("shopping.orders.success")
                .description("Number of successfully placed orders")
                .register(meterRegistry);

        this.orderFailureCounter = Counter.builder("shopping.orders.failure")
                .description("Number of failed orders")
                .register(meterRegistry);

        this.cacheHitCounter = Counter.builder("shopping.cache.hits")
                .description("Number of Redis cache hits")
                .register(meterRegistry);

        this.cacheMissCounter = Counter.builder("shopping.cache.misses")
                .description("Number of Redis cache misses")
                .register(meterRegistry);
    }

    public void incrementOrderSuccess() {
        orderSuccessCounter.increment();
    }

    public void incrementOrderFailure() {
        orderFailureCounter.increment();
    }

    public void incrementCacheHit() {
        cacheHitCounter.increment();
    }

    public void incrementCacheMiss() {
        cacheMissCounter.increment();
    }
}
