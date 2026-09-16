package com.shitanshu.productservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shitanshu.productservice.model.FlashSale;
import com.shitanshu.productservice.service.FlashSaleService;

@RestController
@RequestMapping("/flash-sale")
public class FlashSaleController {

    private final FlashSaleService flashSaleService;

    public FlashSaleController(FlashSaleService flashSaleService) {
        this.flashSaleService = flashSaleService;
    }

    @GetMapping
    public ResponseEntity<?> getActiveFlashSale() {

        FlashSale flashSale = flashSaleService.getActiveFlashSale();

        if (flashSale == null) {
            return ResponseEntity.ok().body(
                    java.util.Map.of(
                            "active", false,
                            "message", "No active flash sale"
                    )
            );
        }

        return ResponseEntity.ok(
                java.util.Map.of(
                        "active", true,
                        "startTime", flashSale.getStartTime(),
                        "endTime", flashSale.getEndTime()
                )
        );
    }
}
