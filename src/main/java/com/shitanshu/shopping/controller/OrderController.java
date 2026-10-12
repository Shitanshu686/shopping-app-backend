package com.shitanshu.shopping.controller;

import com.shitanshu.shopping.concurrency.IdempotencyService;
import com.shitanshu.shopping.dto.CreateOrderRequestDTO;
import com.shitanshu.shopping.dto.OrderResponseDTO;
import com.shitanshu.shopping.dto.UpdateOrderStatusDTO;
import com.shitanshu.shopping.response.ApiResponse;
import com.shitanshu.shopping.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private IdempotencyService idempotencyService;

    // =========================
    // CHECKOUT / CREATE ORDER (IDEMPOTENT & RACE-CONDITION PROOF)
    // =========================
    @PostMapping
    public ResponseEntity<?> createOrder(
            @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey,
            @Valid @RequestBody CreateOrderRequestDTO request,
            Authentication authentication) {

        String email = authentication.getName();

        // 1. Path without Idempotency-Key
        if (idempotencyKey == null || idempotencyKey.isBlank()) {
            OrderResponseDTO order = orderService.createOrder(email, request);
            ApiResponse<OrderResponseDTO> response = new ApiResponse<>(
                    true,
                    "Order placed successfully",
                    order,
                    LocalDateTime.now()
            );
            return ResponseEntity.ok(response);
        }

        // 2. Compute Payload Fingerprint (SHA-256)
        String payloadHash = idempotencyService.computePayloadHash(request.getShippingAddress());
        IdempotencyService.IdempotencyRecord existingRecord = idempotencyService.lockOrGet(idempotencyKey, payloadHash);

        if (existingRecord != null) {
            // Case A: Payload mismatch (reused key with altered params)
            if (!existingRecord.getPayloadHash().equals(payloadHash)) {
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ApiResponse<>(
                        false,
                        "Idempotency key reused with mismatched request parameters",
                        null,
                        LocalDateTime.now()
                ));
            }

            // Case B: Concurrent request still processing
            if (existingRecord.getStatus() == IdempotencyService.Status.PROCESSING) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse<>(
                        false,
                        "Order request with this key is currently in progress. Please retry shortly.",
                        null,
                        LocalDateTime.now()
                ));
            }

            // Case C: Replay completed cached response
            return ResponseEntity.status(existingRecord.getHttpStatusCode())
                    .header("Idempotency-Replayed", "true")
                    .body(existingRecord.getResponseBody());
        }

        try {
            // Process order atomically
            OrderResponseDTO order = orderService.createOrder(email, request);
            ApiResponse<OrderResponseDTO> response = new ApiResponse<>(
                    true,
                    "Order placed successfully",
                    order,
                    LocalDateTime.now()
            );

            // Cache response for replay
            idempotencyService.completeRequest(idempotencyKey, 200, response);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            idempotencyService.releaseKey(idempotencyKey);
            throw e;
        }
    }

    // =========================
    // ORDER HISTORY
    // =========================
    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderResponseDTO>>> getOrderHistory(
            Authentication authentication) {

        String email = authentication.getName();
        List<OrderResponseDTO> orders = orderService.getOrderHistory(email);

        ApiResponse<List<OrderResponseDTO>> response = new ApiResponse<>(
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

        String email = authentication.getName();
        OrderResponseDTO order = orderService.getOrderById(email, orderId);

        ApiResponse<OrderResponseDTO> response = new ApiResponse<>(
                true,
                "Order fetched successfully",
                order,
                LocalDateTime.now()
        );

        return ResponseEntity.ok(response);
    }

    // =========================
    // UPDATE ORDER STATUS (ADMIN ONLY)
    // =========================
    @PutMapping("/{orderId}/status")
    public ResponseEntity<ApiResponse<OrderResponseDTO>> updateOrderStatus(
            @PathVariable Integer orderId,
            @Valid @RequestBody UpdateOrderStatusDTO request) {

        OrderResponseDTO order = orderService.updateOrderStatus(orderId, request);

        ApiResponse<OrderResponseDTO> response = new ApiResponse<>(
                true,
                "Order status updated successfully",
                order,
                LocalDateTime.now()
        );

        return ResponseEntity.ok(response);
    }

    // =========================
    // ADMIN - GET ALL ORDERS
    // =========================
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/admin")
    public ResponseEntity<ApiResponse<List<OrderResponseDTO>>> getAllOrders() {

        List<OrderResponseDTO> orders = orderService.getAllOrders();

        ApiResponse<List<OrderResponseDTO>> response = new ApiResponse<>(
                true,
                "All orders fetched successfully",
                orders,
                LocalDateTime.now()
        );

        return ResponseEntity.ok(response);
    }
}
