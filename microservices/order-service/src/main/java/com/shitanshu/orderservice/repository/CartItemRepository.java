package com.shitanshu.orderservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shitanshu.orderservice.model.Cart;
import com.shitanshu.orderservice.model.CartItem;
import com.shitanshu.orderservice.model.Product;

public interface CartItemRepository
        extends JpaRepository<CartItem, Integer> {

    Optional<CartItem> findByCartAndProduct(
            Cart cart,
            Product product
    );

    List<CartItem> findByCart(
            Cart cart
    );
}