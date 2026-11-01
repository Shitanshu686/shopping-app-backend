package com.shitanshu.shopping.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

@Service
public class BusinessMetricsService {

    private final Counter orderSuccessCounter;
    private final Counter orderFailureCounter;
    private final Counter cacheHitCounter;
    private final Counter cacheMissCounter;

    public BusinessMetricsService(ObjectProvider<MeterRegistry> meterRegistryProvider) {
        MeterRegistry registry = meterRegistryProvider.getIfAvailable(SimpleMeterRegistry::new);

        this.orderSuccessCounter = Counter.builder("shopping.orders.success")
                .description("Number of successfully placed orders")
                .register(registry);

        this.orderFailureCounter = Counter.builder("shopping.orders.failure")
                .description("Number of failed orders")
                .register(registry);

        this.cacheHitCounter = Counter.builder("shopping.cache.hits")
                .description("Number of Redis cache hits")
                .register(registry);

        this.cacheMissCounter = Counter.builder("shopping.cache.misses")
                .description("Number of Redis cache misses")
                .register(registry);
    }

    public void incrementOrderSuccess() {
        if (orderSuccessCounter != null) orderSuccessCounter.increment();
    }

    public void incrementOrderFailure() {
        if (orderFailureCounter != null) orderFailureCounter.increment();
    }

    public void incrementCacheHit() {
        if (cacheHitCounter != null) cacheHitCounter.increment();
    }

    public void incrementCacheMiss() {
        if (cacheMissCounter != null) cacheMissCounter.increment();
    }
}
