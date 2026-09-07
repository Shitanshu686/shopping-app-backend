package com.shitanshu.productservice.repository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import com.shitanshu.productservice.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
public interface ProductRepository extends JpaRepository<Product, Integer>,
JpaSpecificationExecutor<Product> {

    boolean existsByName(String name);

    Page<Product> findByNameContainingIgnoreCase(
            String name,
            Pageable pageable
    );

    Page<Product> findByCategoryIgnoreCase(
            String category,
            Pageable pageable
    );

    Page<Product> findByBrandIgnoreCase(
            String brand,
            Pageable pageable
    );

    Page<Product> findByPriceBetween(
            Double minPrice,
            Double maxPrice,
            Pageable pageable
    );

    Page<Product> findByRatingGreaterThanEqual(
            Double minRating,
            Pageable pageable
    );

    List<Product> findTop4ByCategoryAndIdNot(
            String category,
            Integer id
    );
}
