package com.shitanshu.shopping.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shitanshu.shopping.document.ProductDetailDocument;
import com.shitanshu.shopping.dto.ProductDetailDTO;
import com.shitanshu.shopping.service.ProductDetailService;

@RestController
@RequestMapping("/api/v1/products/details")
public class ProductDetailController {

    @Autowired
    private ProductDetailService productDetailService;

    @PostMapping("/attributes")
    public ResponseEntity<ProductDetailDocument> saveAttributes(@RequestBody ProductDetailDTO request) {
        ProductDetailDocument saved = productDetailService.saveOrUpdateAttributes(
                request.getProductId(), 
                request.getDynamicAttributes()
        );
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDetailDocument> getDetails(@PathVariable Long productId) {
        return productDetailService.getByProductId(productId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{productId}/reviews")
    public ResponseEntity<ProductDetailDocument> addReview(
            @PathVariable Long productId,
            @RequestParam Long userId,
            @RequestParam String userName,
            @RequestParam Integer rating,
            @RequestParam String comment) {
        ProductDetailDocument updated = productDetailService.addReview(productId, userId, userName, rating, comment);
        return ResponseEntity.ok(updated);
    }
}
