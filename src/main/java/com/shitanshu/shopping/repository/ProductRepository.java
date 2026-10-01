package com.shitanshu.shopping.repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.shitanshu.shopping.model.Product;
import java.util.Optional;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


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

    // ==========================================
    // MODULE 32: CONCURRENCY & LOCKING QUERIES
    // ==========================================

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Product p WHERE p.id = :id")
    Optional<Product> findByIdWithPessimisticLock(@Param("id") Integer id);

    @Modifying
    @Query("UPDATE Product p SET p.stock = p.stock - :quantity WHERE p.id = :id AND p.stock >= :quantity")
    int decrementStockAtomic(@Param("id") Integer id, @Param("quantity") Integer quantity);
}
