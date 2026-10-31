package com.shitanshu.shopping.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shitanshu.shopping.health.PolyglotHealthService;

@RestController
@RequestMapping("/api/health")
public class PolyglotHealthController {

    @Autowired
    private PolyglotHealthService polyglotHealthService;

    @GetMapping("/polyglot")
    public ResponseEntity<?> getPolyglotHealth() {
        Map<String, Object> healthStatus = polyglotHealthService.checkHealth();
        
        if ("UP".equals(healthStatus.get("status"))) {
            return ResponseEntity.ok(healthStatus);
        } else {
            return ResponseEntity.status(503).body(healthStatus);
        }
    }
}
