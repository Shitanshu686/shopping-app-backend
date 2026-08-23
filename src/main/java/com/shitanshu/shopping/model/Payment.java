package com.shitanshu.shopping.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    // =========================
    // ORDER
    // =========================

    @OneToOne
    @JoinColumn(
        name = "order_id",
        nullable = false,
        unique = true
    )
    private Order order;


    // =========================
    // RAZORPAY DETAILS
    // =========================

    @Column(
        nullable = false,
        unique = true
    )
    private String razorpayOrderId;


    @Column(
        unique = true
    )
    private String razorpayPaymentId;


    // =========================
    // PAYMENT DETAILS
    // =========================

    @Column(nullable = false)
    private Double amount;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;


    @Column(nullable = false)
    private LocalDateTime createdAt;


    // =========================
    // CONSTRUCTOR
    // =========================

    public Payment() {
    }


    // =========================
    // GETTERS & SETTERS
    // =========================

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }


    public String getRazorpayOrderId() {
        return razorpayOrderId;
    }

    public void setRazorpayOrderId(
            String razorpayOrderId) {

        this.razorpayOrderId =
                razorpayOrderId;
    }


    public String getRazorpayPaymentId() {
        return razorpayPaymentId;
    }

    public void setRazorpayPaymentId(
            String razorpayPaymentId) {

        this.razorpayPaymentId =
                razorpayPaymentId;
    }


    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }


    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(
            PaymentStatus status) {

        this.status = status;
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(
            LocalDateTime createdAt) {

        this.createdAt =
                createdAt;
    }
}