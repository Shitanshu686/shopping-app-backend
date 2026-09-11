package com.shitanshu.paymentservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.shitanshu.paymentservice.model.Payment;

import com.shitanshu.paymentservice.model.Order;

@Repository

public interface PaymentRepository extends JpaRepository<Payment, Integer> {

Optional<Payment> findByOrder(Order order);

Optional<Payment> findByRazorpayOrderId(String razorpayOrderId);

Optional<Payment> findByRazorpayPaymentId(String razorpayPaymentId);

}