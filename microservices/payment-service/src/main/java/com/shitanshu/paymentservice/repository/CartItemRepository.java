package com.shitanshu.paymentservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shitanshu.paymentservice.model.Cart;
import com.shitanshu.paymentservice.model.CartItem;
import com.shitanshu.paymentservice.model.Product;

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