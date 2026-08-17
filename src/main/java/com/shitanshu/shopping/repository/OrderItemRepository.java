package com.shitanshu.shopping.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shitanshu.shopping.model.Order;
import com.shitanshu.shopping.model.OrderItem;

@Repository
public interface OrderItemRepository
        extends JpaRepository<OrderItem, Integer> {

    List<OrderItem> findByOrder(Order order);

}