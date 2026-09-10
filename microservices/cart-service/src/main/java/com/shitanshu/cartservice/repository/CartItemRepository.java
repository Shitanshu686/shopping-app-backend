package com.shitanshu.cartservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shitanshu.cartservice.model.Cart;
import com.shitanshu.cartservice.model.CartItem;
import com.shitanshu.cartservice.model.Product;

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