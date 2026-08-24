package com.shitanshu.shopping.dto;

import java.time.LocalDateTime;

import com.shitanshu.shopping.model.OrderStatus;

public class AdminRecentOrderDTO {

    private Integer orderId;

    private String customerName;

    private Double totalAmount;

    private OrderStatus status;

    private LocalDateTime createdAt;


    public AdminRecentOrderDTO() {
    }


    public AdminRecentOrderDTO(
            Integer orderId,
            String customerName,
            Double totalAmount,
            OrderStatus status,
            LocalDateTime createdAt) {

        this.orderId = orderId;
        this.customerName = customerName;
        this.totalAmount = totalAmount;
        this.status = status;
        this.createdAt = createdAt;
    }


    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }


    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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