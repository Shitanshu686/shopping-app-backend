package com.shitanshu.shopping.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shitanshu.shopping.model.Product;
import com.shitanshu.shopping.model.ProductSpecification;

@Repository
public interface ProductSpecificationRepository
        extends JpaRepository<ProductSpecification, Integer> {

    List<ProductSpecification> findByProduct(Product product);

    boolean existsByProductAndSpecificationName(
            Product product,
            String specificationName
    );
}