package com.shitanshu.shopping.service;

import com.shitanshu.shopping.dto.ProductResponseDTO;
import com.shitanshu.shopping.exception.ProductNotFoundException;
import com.shitanshu.shopping.model.Product;
import com.shitanshu.shopping.repository.ProductRepository;
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
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product sampleProduct;

    @BeforeEach
    void setUp() {
        sampleProduct = new Product();
        sampleProduct.setId(1);
        sampleProduct.setName("Wireless Mouse");
        sampleProduct.setBrand("Logitech");
        sampleProduct.setPrice(1299.0);
        sampleProduct.setCategory("Electronics");
        sampleProduct.setStock(50);
        sampleProduct.setRating(4.5);
    }

    @Test
    void getProductById_Success() {
        when(productRepository.findById(1)).thenReturn(Optional.of(sampleProduct));

        ProductResponseDTO response = productService.getProductById(1);

        assertNotNull(response);
        assertEquals(1, response.getId());
        assertEquals("Wireless Mouse", response.getName());
        assertEquals(1299.0, response.getPrice());
        verify(productRepository, times(1)).findById(1);
    }

    @Test
    void getProductById_NotFound() {
        when(productRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> {
            productService.getProductById(99);
        });

        verify(productRepository, times(1)).findById(99);
    }
}
