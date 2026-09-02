package com.shitanshu.shopping.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shitanshu.shopping.model.ProductRating;
import java.util.List;
@Repository
public interface ProductRatingRepository
        extends JpaRepository<ProductRating, Integer> {

    Optional<ProductRating> findByProductIdAndUserId(
            Integer productId,
            Integer userId
    );
    List<ProductRating> findByProductId(Integer productId);

}