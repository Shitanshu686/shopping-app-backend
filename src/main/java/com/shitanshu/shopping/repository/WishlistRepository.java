package com.shitanshu.shopping.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shitanshu.shopping.model.User;
import com.shitanshu.shopping.model.Wishlist;

public interface WishlistRepository
        extends JpaRepository<Wishlist, Integer> {

    Optional<Wishlist> findByUser(User user);
}