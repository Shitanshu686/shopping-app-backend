package com.shitanshu.shopping.service;

import com.shitanshu.shopping.dto.CreateOrderRequestDTO;
import com.shitanshu.shopping.exception.UserNotFoundException;
import com.shitanshu.shopping.model.User;
import com.shitanshu.shopping.repository.OrderRepository;
import com.shitanshu.shopping.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private OrderService orderService;

    private User sampleUser;
    private CreateOrderRequestDTO orderRequest;

    @BeforeEach
    void setUp() {
        sampleUser = new User();
        sampleUser.setId(1L);
        sampleUser.setEmail("shitanshu@example.com");

        orderRequest = new CreateOrderRequestDTO();
    }

    @Test
    void createOrder_UserNotFound_ThrowsException() {
        when(userRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            orderService.createOrder("unknown@example.com", orderRequest);
        });

        verify(userRepository, times(1)).findByEmail("unknown@example.com");
        verifyNoInteractions(orderRepository);
    }
}
