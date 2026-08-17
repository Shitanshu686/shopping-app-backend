package com.shitanshu.shopping.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    // =========================
    // USER
    // =========================

    @ManyToOne
    @JoinColumn(
        name = "user_id",
        nullable = false
    )
    private User user;


    // =========================
    // SHIPPING ADDRESS
    // =========================

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String state;

    @Column(nullable = false)
    private String pincode;


    // =========================
    // ORDER DETAILS
    // =========================

    @Column(nullable = false)
    private Double totalAmount;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;


    @Column(nullable = false)
    private LocalDateTime createdAt;


    // =========================
    // CONSTRUCTOR
    // =========================

    public Order() {
    }


    public Order(
            Integer id,
            User user,
            String fullName,
            String phone,
            String address,
            String city,
            String state,
            String pincode,
            Double totalAmount,
            OrderStatus status,
            LocalDateTime createdAt) {

        this.id = id;
        this.user = user;
        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
        this.city = city;
        this.state = state;
        this.pincode = pincode;
        this.totalAmount = totalAmount;
        this.status = status;
        this.createdAt = createdAt;
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


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }


    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }


    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }


    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }


    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}