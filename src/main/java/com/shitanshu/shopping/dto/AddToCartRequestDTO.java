package com.shitanshu.shopping.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class AddToCartRequestDTO {

    @NotNull(message = "Product ID is required")
    private Integer productId;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    private Boolean flashSale = false;

    public AddToCartRequestDTO() {
    }

    public AddToCartRequestDTO(Integer productId, Integer quantity, Boolean flashSale) {
        this.productId = productId;
        this.quantity = quantity;
        this.flashSale = flashSale != null ? flashSale : false;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Boolean getFlashSale() {
        return flashSale != null ? flashSale : false;
    }

    public void setFlashSale(Boolean flashSale) {
        this.flashSale = flashSale != null ? flashSale : false;
    }
}
