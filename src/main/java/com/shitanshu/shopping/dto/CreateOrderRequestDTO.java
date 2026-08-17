package com.shitanshu.shopping.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class CreateOrderRequestDTO {

    @NotNull(message = "Shipping address is required")
    @Valid
    private ShippingAddressDTO shippingAddress;


    public CreateOrderRequestDTO() {
    }


    public ShippingAddressDTO getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(
            ShippingAddressDTO shippingAddress) {

        this.shippingAddress = shippingAddress;
    }
}