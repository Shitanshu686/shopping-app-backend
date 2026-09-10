package com.shitanshu.cartservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shitanshu.cartservice.model.Cart;
import com.shitanshu.cartservice.model.User;

public interface CartRepository extends JpaRepository<Cart, Integer> {

    Optional<Cart> findByUser(User user);

}