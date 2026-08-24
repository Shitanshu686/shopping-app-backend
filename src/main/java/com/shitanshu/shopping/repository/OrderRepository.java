package com.shitanshu.shopping.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shitanshu.shopping.model.Order;
import com.shitanshu.shopping.model.User;

@Repository
public interface OrderRepository
        extends JpaRepository<Order, Integer> {

    @Query("SELECT o FROM Order o WHERE o.user = :user ORDER BY o.createdAt desc")
	List<Order> findByUserOrderByCreatedAtDesc(User user);
    @Query("""
            SELECT o
            FROM Order o
            ORDER BY o.createdAt DESC
            """)
    List<Order> findRecentOrders();


    @Query("""
            SELECT COALESCE(SUM(o.totalAmount), 0)
            FROM Order o
            WHERE o.status = com.shitanshu.shopping.model.OrderStatus.CONFIRMED
            """)
    double getTotalRevenue();

}