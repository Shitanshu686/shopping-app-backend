package com.shitanshu.shopping.controller;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/flash-sale")
@CrossOrigin(origins = "*")
public class FlashSaleController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getFlashSaleStatus() {
        Map<String, Object> response = new HashMap<>();

        try {
            String sql = "SELECT active, end_time FROM flash_sale WHERE active = 1 ORDER BY id DESC LIMIT 1";
            Map<String, Object> row = jdbcTemplate.queryForMap(sql);

            response.put("active", ((Number) row.get("active")).intValue() == 1);
            response.put("endTime", row.get("end_time").toString());
        } catch (Exception e) {
            // Agar table empty ho toh current time se 6 ghante baad ka default timer
            LocalDateTime defaultEnd = LocalDateTime.now().plusHours(6);
            response.put("active", true);
            response.put("endTime", defaultEnd.atZone(ZoneId.systemDefault()).toInstant().toString());
        }

        return ResponseEntity.ok(response);
    }
}
