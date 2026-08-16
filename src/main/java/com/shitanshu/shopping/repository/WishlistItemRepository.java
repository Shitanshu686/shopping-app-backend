package com.shitanshu.shopping.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shitanshu.shopping.model.Product;
import com.shitanshu.shopping.model.Wishlist;
import com.shitanshu.shopping.model.WishlistItem;

public interface WishlistItemRepository
        extends JpaRepository<WishlistItem, Integer> {

    Optional<WishlistItem> findByWishlistAndProduct(
            Wishlist wishlist,
            Product product);
}