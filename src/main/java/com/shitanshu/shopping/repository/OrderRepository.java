package com.shitanshu.shopping.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shitanshu.shopping.model.Order;
import com.shitanshu.shopping.model.User;

@Repository
public interface OrderRepository
        extends JpaRepository<Order, Integer> {

    List<Order> findByUserOrderByCreatedAtDesc(User user);

}