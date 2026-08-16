package com.shitanshu.shopping.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.shitanshu.shopping.dto.WishlistResponseDTO;
import com.shitanshu.shopping.response.ApiResponse;
import com.shitanshu.shopping.service.WishlistService;

@RestController
@RequestMapping("/wishlist")
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;


    // =========================
    // ADD TO WISHLIST
    // =========================

    @PostMapping("/{productId}")
    public ResponseEntity<ApiResponse<WishlistResponseDTO>> addToWishlist(
            @PathVariable Integer productId,
            Authentication authentication) {

        String email = authentication.getName();

        WishlistResponseDTO wishlist =
                wishlistService.addToWishlist(
                        email,
                        productId);

        ApiResponse<WishlistResponseDTO> response =
                new ApiResponse<>(
                        true,
                        "Product added to wishlist successfully",
                        wishlist,
                        LocalDateTime.now());

        return ResponseEntity.ok(response);
    }


    // =========================
    // VIEW WISHLIST
    // =========================

    @GetMapping
    public ResponseEntity<ApiResponse<WishlistResponseDTO>> getWishlist(
            Authentication authentication) {

        String email = authentication.getName();

        WishlistResponseDTO wishlist =
                wishlistService.getWishlist(email);

        ApiResponse<WishlistResponseDTO> response =
                new ApiResponse<>(
                        true,
                        "Wishlist fetched successfully",
                        wishlist,
                        LocalDateTime.now());

        return ResponseEntity.ok(response);
    }


    // =========================
    // REMOVE FROM WISHLIST
    // =========================

    @DeleteMapping("/{itemId}")
    public ResponseEntity<ApiResponse<WishlistResponseDTO>> removeFromWishlist(
            @PathVariable Integer itemId,
            Authentication authentication) {

        String email = authentication.getName();

        WishlistResponseDTO wishlist =
                wishlistService.removeFromWishlist(
                        email,
                        itemId);

        ApiResponse<WishlistResponseDTO> response =
                new ApiResponse<>(
                        true,
                        "Product removed from wishlist successfully",
                        wishlist,
                        LocalDateTime.now());

        return ResponseEntity.ok(response);
    }
}