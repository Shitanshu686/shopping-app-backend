package com.shitanshu.paymentservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shitanshu.paymentservice.model.Cart;
import com.shitanshu.paymentservice.model.User;

public interface CartRepository extends JpaRepository<Cart, Integer> {

    Optional<Cart> findByUser(User user);

}