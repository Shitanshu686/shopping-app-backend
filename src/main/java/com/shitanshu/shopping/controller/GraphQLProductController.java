package com.shitanshu.shopping.controller;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import com.shitanshu.shopping.model.Product;
import com.shitanshu.shopping.repository.ProductRepository;

@Controller
public class GraphQLProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @QueryMapping
    @Transactional(readOnly = true)
    public List<Product> allProducts() {
        try {
            return productRepository.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @QueryMapping
    @Transactional(readOnly = true)
    public Product productById(@Argument Integer id) {
        return productRepository.findById(id).orElse(null);
    }

    @QueryMapping
    @Transactional(readOnly = true)
    public List<Product> productsByCategory(@Argument String category) {
        return productRepository.findAll().stream()
                .filter(p -> category.equalsIgnoreCase(p.getCategory()))
                .toList();
    }

    @QueryMapping
    public Map<String, Object> flashSaleStatus() {
        try {
            String sql = "SELECT active, end_time FROM flash_sale WHERE active = 1 ORDER BY id DESC LIMIT 1";
            Map<String, Object> row = jdbcTemplate.queryForMap(sql);
            return Map.of(
                "active", ((Number) row.get("active")).intValue() == 1,
                "endTime", row.get("end_time").toString()
            );
        } catch (Exception e) {
            LocalDateTime defaultEnd = LocalDateTime.now().plusHours(6);
            return Map.of(
                "active", true,
                "endTime", defaultEnd.atZone(ZoneId.systemDefault()).toInstant().toString()
            );
        }
    }

    @MutationMapping
    @Transactional
    public Product addProduct(@Argument("input") Map<String, Object> input) {
        Product p = new Product();
        p.setName((String) input.get("name"));
        p.setDescription((String) input.get("description"));

        double price = input.get("price") != null ? ((Number) input.get("price")).doubleValue() : 0.0;
        p.setPrice(price);

        double oldPrice = input.get("oldPrice") != null 
                ? ((Number) input.get("oldPrice")).doubleValue() 
                : (price > 0 ? price * 1.2 : 0.0);
        p.setOldPrice(oldPrice);

        double rating = input.get("rating") != null 
                ? ((Number) input.get("rating")).doubleValue() 
                : 4.5;
        p.setRating(rating);

        p.setCategory(input.get("category") != null ? (String) input.get("category") : "General");
        p.setBrand(input.get("brand") != null ? (String) input.get("brand") : "Generic");
        p.setStock(input.get("stock") != null ? ((Number) input.get("stock")).intValue() : 10);
        p.setImage(input.get("image") != null ? (String) input.get("image") : "default.png");

        return productRepository.save(p);
    }

    @MutationMapping
    @Transactional
    public Boolean deleteProduct(@Argument Integer id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
