package com.shitanshu.shopping.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shitanshu.shopping.model.Product;

@Repository
public interface ProductRepository
        extends JpaRepository<Product, Integer> {

    boolean existsByName(String name);

    List<Product> findTop4ByCategoryAndIdNot(
            String category,
            Integer id
    );

    long countByStockBetween(
            Integer minStock,
            Integer maxStock
    );

    long countByStock(
            Integer stock
    );
}