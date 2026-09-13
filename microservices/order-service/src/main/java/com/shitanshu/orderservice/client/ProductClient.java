package com.shitanshu.orderservice.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.shitanshu.orderservice.model.Product;

@Component
public class ProductClient {

    private final RestClient restClient;

    public ProductClient() {
        this.restClient = RestClient.builder()
                .baseUrl("http://localhost:8081")
                .build();
    }

    public Product getProductById(Integer productId) {

        return restClient.get()
                .uri("/products/{id}", productId)
                .retrieve()
                .body(Product.class);
    }
}
