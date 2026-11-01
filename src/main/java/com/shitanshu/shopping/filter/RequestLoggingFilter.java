package com.shitanshu.shopping.filter;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        Instant startTime = Instant.now();
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String clientIp = request.getRemoteAddr();

        log.info("[HTTP-IN] Incoming Request: {} {} from IP: {}", method, uri, clientIp);

        try {
            filterChain.doFilter(request, response);
        } finally {
            Duration duration = Duration.between(startTime, Instant.now());
            int status = response.getStatus();
            
            log.info("[HTTP-OUT] Completed: {} {} | Status: {} | Duration: {}ms", 
                    method, uri, status, duration.toMillis());
        }
    }
}
