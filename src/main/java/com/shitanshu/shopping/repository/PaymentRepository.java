package com.shitanshu.shopping.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.shitanshu.shopping.model.Payment;

import com.shitanshu.shopping.model.Order;

@Repository

public interface PaymentRepository extends JpaRepository<Payment, Integer> {

Optional<Payment> findByOrder(Order order);

Optional<Payment> findByRazorpayOrderId(String razorpayOrderId);

Optional<Payment> findByRazorpayPaymentId(String razorpayPaymentId);

}