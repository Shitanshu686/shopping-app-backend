package com.shitanshu.shopping.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import com.shitanshu.shopping.service.ProductRatingService;
import java.time.LocalDateTime;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.security.core.Authentication;

import jakarta.validation.Valid;

import com.shitanshu.shopping.dto.ProductRatingRequestDTO;
import com.shitanshu.shopping.model.ProductRating;
import com.shitanshu.shopping.response.ApiResponse;
@RestController
@RequestMapping("/ratings")
public class ProductRatingController {

    @Autowired
    private ProductRatingService productRatingService;
    @PostMapping
    public ResponseEntity<ApiResponse<ProductRating>> addRating(
            @Valid @RequestBody ProductRatingRequestDTO request,
            Authentication authentication) {


        String email =
                authentication.getName();

        ProductRating rating =
                productRatingService.saveOrUpdateRating(
                        email,
                        request.getProductId(),
                        request.getRating()
                );

        ApiResponse<ProductRating> response =
                new ApiResponse<>(
                        true,
                        "Product rating submitted successfully",
                        rating,
                        LocalDateTime.now()
                );

        return ResponseEntity.ok(response);
    }
    @GetMapping("/can-rate/{productId}")
    public ResponseEntity<ApiResponse<Boolean>> canUserRateProduct(
            @PathVariable Integer productId,
            Authentication authentication) {

        String email = authentication.getName();

        boolean canRate =
                productRatingService.canUserRateProduct(
                        email,
                        productId
                );

        ApiResponse<Boolean> response =
                new ApiResponse<>(
                        true,
                        "Rating eligibility checked successfully",
                        canRate,
                        LocalDateTime.now()
                );

        return ResponseEntity.ok(response);
    }
    @GetMapping("/my-rating/{productId}")
    public ResponseEntity<ApiResponse<ProductRating>> getMyRating(
            @PathVariable Integer productId,
            Authentication authentication) {

        String email = authentication.getName();

        ProductRating rating =
                productRatingService.getMyRating(
                        email,
                        productId
                );

        ApiResponse<ProductRating> response =
                new ApiResponse<>(
                        true,
                        "User rating fetched successfully",
                        rating,
                        LocalDateTime.now()
                );

        return ResponseEntity.ok(response);
    }
    @GetMapping("/product/{productId}")
    public ResponseEntity<ApiResponse<Double>> getProductAverageRating(
            @PathVariable Integer productId) {

        Double averageRating =
                productRatingService
                        .getProductAverageRating(productId);

        ApiResponse<Double> response =
                new ApiResponse<>(
                        true,
                        "Product average rating fetched successfully",
                        averageRating,
                        LocalDateTime.now()
                );

        return ResponseEntity.ok(response);
    }
}