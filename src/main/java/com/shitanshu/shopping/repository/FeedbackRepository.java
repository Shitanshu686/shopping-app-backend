package com.shitanshu.shopping.repository;

import com.shitanshu.shopping.model.Feedback;
import com.shitanshu.shopping.model.Product;
import com.shitanshu.shopping.model.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FeedbackRepository
        extends JpaRepository<Feedback, Integer> {

    // ======================
    // GET PRODUCT FEEDBACK
    // ======================

    List<Feedback> findByProduct(Product product);


    // ======================
    // CHECK USER FEEDBACK
    // ======================

    Optional<Feedback> findByUserAndProduct(
            User user,
            Product product
    );


    // ======================
    // CHECK DUPLICATE
    // ======================

    boolean existsByUserAndProduct(
            User user,
            Product product
    );
}