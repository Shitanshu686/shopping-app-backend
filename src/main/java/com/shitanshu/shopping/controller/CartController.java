package com.shitanshu.shopping.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import com.shitanshu.shopping.dto.AddToCartRequestDTO;
import com.shitanshu.shopping.dto.CartResponseDTO;
import com.shitanshu.shopping.dto.UpdateCartItemDTO;
import com.shitanshu.shopping.response.ApiResponse;
import com.shitanshu.shopping.service.CartService;

@RestController
@RequestMapping("/cart")
@CrossOrigin(origins = "*")
public class CartController {

    @Autowired
    private CartService cartService;


    // =========================
    // ADD TO CART
    // =========================

    @PostMapping
    public ResponseEntity<ApiResponse<CartResponseDTO>> addToCart(
            @Valid @RequestBody AddToCartRequestDTO request,
            Authentication authentication) {

        String email = authentication.getName();

        CartResponseDTO cart =
                cartService.addToCart(email, request);

        ApiResponse<CartResponseDTO> response =
                new ApiResponse<>(
                        true,
                        "Product added to cart successfully",
                        cart,
                        LocalDateTime.now());

        return ResponseEntity.ok(response);
    }


    // =========================
    // GET CART
    // =========================

    @GetMapping
    public ResponseEntity<ApiResponse<CartResponseDTO>> getCart(
            Authentication authentication) {

        String email = authentication.getName();

        CartResponseDTO cart =
                cartService.getCart(email);

        ApiResponse<CartResponseDTO> response =
                new ApiResponse<>(
                        true,
                        "Cart fetched successfully",
                        cart,
                        LocalDateTime.now());

        return ResponseEntity.ok(response);
    }


    // =========================
    // UPDATE QUANTITY
    // =========================

    @PutMapping("/{itemId}")
    public ResponseEntity<ApiResponse<CartResponseDTO>> updateQuantity(
            @PathVariable Integer itemId,
            @Valid @RequestBody UpdateCartItemDTO request,
            Authentication authentication) {

        String email = authentication.getName();

        CartResponseDTO cart =
                cartService.updateQuantity(
                        email,
                        itemId,
                        request);

        ApiResponse<CartResponseDTO> response =
                new ApiResponse<>(
                        true,
                        "Cart quantity updated successfully",
                        cart,
                        LocalDateTime.now());

        return ResponseEntity.ok(response);
    }


    // =========================
    // REMOVE ITEM
    // =========================

    @DeleteMapping("/{itemId}")
    public ResponseEntity<ApiResponse<CartResponseDTO>> removeItem(
            @PathVariable Integer itemId,
            Authentication authentication) {

        String email = authentication.getName();

        CartResponseDTO cart =
                cartService.removeItem(
                        email,
                        itemId);

        ApiResponse<CartResponseDTO> response =
                new ApiResponse<>(
                        true,
                        "Product removed from cart successfully",
                        cart,
                        LocalDateTime.now());

        return ResponseEntity.ok(response);
    }
}