package com.shitanshu.orderservice.event;

public class OrderCreatedEvent {

    private Integer orderId;
    private String email;
    private Double totalAmount;

    public OrderCreatedEvent() {
    }

    public OrderCreatedEvent(Integer orderId, String email, Double totalAmount) {
        this.orderId = orderId;
        this.email = email;
        this.totalAmount = totalAmount;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
