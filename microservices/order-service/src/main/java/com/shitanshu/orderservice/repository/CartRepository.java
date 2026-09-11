package com.shitanshu.orderservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shitanshu.orderservice.model.Cart;
import com.shitanshu.orderservice.model.User;

public interface CartRepository extends JpaRepository<Cart, Integer> {

    Optional<Cart> findByUser(User user);

}