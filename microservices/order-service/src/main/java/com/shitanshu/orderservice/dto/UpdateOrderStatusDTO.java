package com.shitanshu.orderservice.dto;

import com.shitanshu.orderservice.model.OrderStatus;

import jakarta.validation.constraints.NotNull;

public class UpdateOrderStatusDTO {

    @NotNull(message = "Order status is required")
    private OrderStatus status;


    public UpdateOrderStatusDTO() {
    }


    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}