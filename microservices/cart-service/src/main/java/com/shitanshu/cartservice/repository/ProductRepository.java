package com.shitanshu.cartservice.repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.shitanshu.cartservice.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>,
JpaSpecificationExecutor<Product> { 

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
    Page<Product> findAll(Pageable pageable);
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
}