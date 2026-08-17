package com.shitanshu.shopping.dto;

import com.shitanshu.shopping.model.OrderStatus;

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