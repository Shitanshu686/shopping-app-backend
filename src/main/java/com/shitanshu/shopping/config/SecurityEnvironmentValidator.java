package com.shitanshu.shopping.config;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityEnvironmentValidator {

    private static final Logger log = LoggerFactory.getLogger(SecurityEnvironmentValidator.class);

    @PostConstruct
    public void validateSecrets() {
        String razorpaySecret = System.getenv("RAZORPAY_KEY_SECRET");
        if (razorpaySecret == null || razorpaySecret.trim().isEmpty()) {
            log.warn("[SECURITY-WARNING] RAZORPAY_KEY_SECRET environment variable is not set or empty!");
        } else {
            log.info("[SECURITY] RAZORPAY_KEY_SECRET is successfully loaded and validated.");
        }
    }
}
