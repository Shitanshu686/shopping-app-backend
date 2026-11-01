package com.shitanshu.shopping.ratelimit;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RateLimitingInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(RateLimitingInterceptor.class);
    private final Map<String, RequestCounter> requestCounts = new ConcurrentHashMap<>();
    
    // Limit: Max 20 requests per minute per IP
    private static final int MAX_REQUESTS_PER_MINUTE = 20;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String clientIp = getClientIp(request);
        long currentTimeWindow = System.currentTimeMillis() / 60000; // 1-minute window

        RequestCounter counter = requestCounts.computeIfAbsent(clientIp, k -> new RequestCounter(currentTimeWindow, new AtomicInteger(0)));

        synchronized (counter) {
            if (counter.timestamp != currentTimeWindow) {
                counter.timestamp = currentTimeWindow;
                counter.count.set(0);
            }

            if (counter.count.incrementAndGet() > MAX_REQUESTS_PER_MINUTE) {
                log.warn("[RATE-LIMIT] Rate limit exceeded for IP: {}", clientIp);
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Too Many Requests\", \"message\": \"Rate limit exceeded. Please try again later.\"}");
                return false;
            }
        }

        return true;
    }

    private String getClientIp(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }

    private static class RequestCounter {
        long timestamp;
        AtomicInteger count;

        public RequestCounter(long timestamp, AtomicInteger count) {
            this.timestamp = timestamp;
            this.count = count;
        }
    }
}
