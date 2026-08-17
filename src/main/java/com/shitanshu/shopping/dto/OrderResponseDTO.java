package com.shitanshu.shopping.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.shitanshu.shopping.model.OrderStatus;

public class OrderResponseDTO {

    private Integer orderId;

    private String fullName;

    private String phone;

    private String address;

    private String city;

    private String state;

    private String pincode;

    private Double totalAmount;

    private OrderStatus status;

    private LocalDateTime createdAt;

    private List<OrderItemResponseDTO> items;


    public OrderResponseDTO() {
    }


    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
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


    public List<OrderItemResponseDTO> getItems() {
        return items;
    }

    public void setItems(
            List<OrderItemResponseDTO> items) {

        this.items = items;
    }
}