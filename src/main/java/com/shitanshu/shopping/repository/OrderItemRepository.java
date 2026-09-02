package com.shitanshu.shopping.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shitanshu.shopping.model.Order;
import com.shitanshu.shopping.model.OrderItem;
import org.springframework.data.jpa.repository.Query;
import com.shitanshu.shopping.model.OrderStatus;

@Repository
public interface OrderItemRepository
        extends JpaRepository<OrderItem, Integer> {

    List<OrderItem> findByOrder(Order order);

    @Query("""
            SELECT COUNT(oi) > 0
            FROM OrderItem oi
            WHERE oi.order.user.id = :userId
            AND oi.productId = :productId
            AND oi.order.status = :status
            """)
    boolean existsPurchasedProduct(
            Integer userId,
            Integer productId,
            OrderStatus status
    );

}