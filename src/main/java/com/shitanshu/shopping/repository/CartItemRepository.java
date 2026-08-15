package com.shitanshu.shopping.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shitanshu.shopping.model.Cart;
import com.shitanshu.shopping.model.CartItem;
import com.shitanshu.shopping.model.Product;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);

}