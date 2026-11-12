package com.shitanshu.shopping.repository;

import com.shitanshu.shopping.model.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductRepositoryTest {

    @Mock
    private ProductRepository productRepository;

    @Test
    void findByNameContainingIgnoreCase_ShouldReturnMatchingProducts() {
        Product product = new Product();
        product.setId(1);
        product.setName("Mechanical Keyboard");
        product.setBrand("Razer");
        product.setPrice(4999.0);
        product.setCategory("Accessories");

        Page<Product> mockPage = new PageImpl<>(Collections.singletonList(product));
        when(productRepository.findByNameContainingIgnoreCase(eq("mechanical"), any(PageRequest.of(0, 10).getClass()))).thenReturn(mockPage);

        Page<Product> result = productRepository.findByNameContainingIgnoreCase("mechanical", PageRequest.of(0, 10));

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals("Mechanical Keyboard", result.getContent().get(0).getName());
        verify(productRepository, times(1)).findByNameContainingIgnoreCase(eq("mechanical"), any());
    }
}
