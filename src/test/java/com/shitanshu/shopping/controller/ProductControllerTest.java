package com.shitanshu.shopping.controller;

import com.shitanshu.shopping.dto.ProductResponseDTO;
import com.shitanshu.shopping.response.ApiResponse;
import com.shitanshu.shopping.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private ProductResponseDTO sampleDto;

    @BeforeEach
    void setUp() {
        sampleDto = new ProductResponseDTO();
        sampleDto.setId(1);
        sampleDto.setName("Wireless Mouse");
        sampleDto.setPrice(1299.0);
    }

    @Test
    void getProductById_ShouldReturnProductResponse() {
        when(productService.getProductById(eq(1))).thenReturn(sampleDto);

        ResponseEntity<ApiResponse<ProductResponseDTO>> response = productController.getProductById(1);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getData());
        assertEquals(1, response.getBody().getData().getId());
        assertEquals("Wireless Mouse", response.getBody().getData().getName());
        assertEquals(1299.0, response.getBody().getData().getPrice());
        verify(productService, times(1)).getProductById(eq(1));
    }
}
