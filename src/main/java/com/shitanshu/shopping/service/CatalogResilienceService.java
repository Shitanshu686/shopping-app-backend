package com.shitanshu.shopping.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;

@Service
public class CatalogResilienceService {

    private static final Logger log = LoggerFactory.getLogger(CatalogResilienceService.class);

    private static final String CATALOG_SERVICE = "catalogService";

    @RateLimiter(name = CATALOG_SERVICE, fallbackMethod = "rateLimitFallback")
    @Bulkhead(name = CATALOG_SERVICE, type = Bulkhead.Type.SEMAPHORE, fallbackMethod = "bulkheadFallback")
    public String getProductCatalogDetails(Long productId) {
        log.info("[BULKHEAD-RATELIMIT] Fetching product catalog details for ID: {}", productId);
        return "SUCCESS - Product catalog details loaded for ID: " + productId;
    }

    public String rateLimitFallback(Long productId, Throwable t) {
        log.warn("[RATE-LIMITER-FALLBACK] Too many requests for product ID: {}. Reason: {}", productId, t.getMessage());
        return "RATE_LIMIT_EXCEEDED - Please slow down and try again in a few moments.";
    }

    public String bulkheadFallback(Long productId, Throwable t) {
        log.warn("[BULKHEAD-FALLBACK] Concurrent execution limit reached for product ID: {}. Reason: {}", productId, t.getMessage());
        return "BULKHEAD_FULL - System is experiencing high concurrent load. Please try shortly.";
    }
}
