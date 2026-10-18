package com.shitanshu.shopping.event;

import java.time.LocalDateTime;

public class OrderPlacedEvent {

    private Integer orderId;
    private Integer userId;
    private String userEmail;
    private Double totalAmount;
    private String status;
    private LocalDateTime timestamp;

    public OrderPlacedEvent() {
    }

    public OrderPlacedEvent(Integer orderId, Integer userId, String userEmail, Double totalAmount, String status) {
        this.orderId = orderId;
        this.userId = userId;
        this.userEmail = userEmail;
        this.totalAmount = totalAmount;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "OrderPlacedEvent{" +
                "orderId=" + orderId +
                ", userId=" + userId +
                ", userEmail='" + userEmail + '\'' +
                ", totalAmount=" + totalAmount +
                ", status='" + status + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
