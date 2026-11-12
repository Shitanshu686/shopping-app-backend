package com.shitanshu.shopping.controller;

import com.shitanshu.shopping.concurrency.IdempotencyService;
import com.shitanshu.shopping.dto.CreateOrderRequestDTO;
import com.shitanshu.shopping.dto.OrderResponseDTO;
import com.shitanshu.shopping.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @Mock
    private IdempotencyService idempotencyService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private OrderController orderController;

    private CreateOrderRequestDTO requestDTO;
    private OrderResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        requestDTO = new CreateOrderRequestDTO();

        responseDTO = new OrderResponseDTO();
        responseDTO.setOrderId(101);
    }

    @Test
    void createOrder_ShouldReturnCreatedOrderResponse() {
        when(authentication.getName()).thenReturn("shitanshu@example.com");
        when(idempotencyService.computePayloadHash(any())).thenReturn("dummy-hash");
        when(orderService.createOrder(eq("shitanshu@example.com"), any(CreateOrderRequestDTO.class)))
                .thenReturn(responseDTO);

        ResponseEntity<?> response = 
                orderController.createOrder("shitanshu@example.com", requestDTO, authentication);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        verify(orderService, times(1)).createOrder(eq("shitanshu@example.com"), any(CreateOrderRequestDTO.class));
    }
}
