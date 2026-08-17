package com.shitanshu.shopping.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import com.shitanshu.shopping.dto.CreateOrderRequestDTO;
import com.shitanshu.shopping.dto.OrderResponseDTO;
import com.shitanshu.shopping.response.ApiResponse;
import com.shitanshu.shopping.service.OrderService;

@RestController
@RequestMapping("/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    private OrderService orderService;


    // =========================
    // CHECKOUT / CREATE ORDER
    // =========================

    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponseDTO>> createOrder(
            @Valid @RequestBody CreateOrderRequestDTO request,
            Authentication authentication) {

        String email =
                authentication.getName();

        OrderResponseDTO order =
                orderService.createOrder(
                        email,
                        request
                );

        ApiResponse<OrderResponseDTO> response =
                new ApiResponse<>(
                        true,
                        "Order placed successfully",
                        order,
                        LocalDateTime.now()
                );

        return ResponseEntity.ok(response);
    }


    // =========================
    // ORDER HISTORY
    // =========================

    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderResponseDTO>>> getOrderHistory(
            Authentication authentication) {

        String email =
                authentication.getName();

        List<OrderResponseDTO> orders =
                orderService.getOrderHistory(
                        email
                );

        ApiResponse<List<OrderResponseDTO>> response =
                new ApiResponse<>(
                        true,
                        "Order history fetched successfully",
                        orders,
                        LocalDateTime.now()
                );

        return ResponseEntity.ok(response);
    }


    // =========================
    // GET SINGLE ORDER
    // =========================

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderResponseDTO>> getOrderById(
            @PathVariable Integer orderId,
            Authentication authentication) {

        String email =
                authentication.getName();

        OrderResponseDTO order =
                orderService.getOrderById(
                        email,
                        orderId
                );

        ApiResponse<OrderResponseDTO> response =
                new ApiResponse<>(
                        true,
                        "Order fetched successfully",
                        order,
                        LocalDateTime.now()
                );

        return ResponseEntity.ok(response);
    }

}