package com.shitanshu.cartservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class AddToCartRequestDTO {

    @NotNull
    private Integer productId;

    @NotNull
    @Min(1)
    private Integer quantity;

    private boolean flashSale;

    public AddToCartRequestDTO() {
    }

    public AddToCartRequestDTO(
            Integer productId,
            Integer quantity,
            boolean flashSale) {

        this.productId = productId;
        this.quantity = quantity;
        this.flashSale = flashSale;
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

    public boolean isFlashSale() {
        return flashSale;
    }

    public void setFlashSale(boolean flashSale) {
        this.flashSale = flashSale;
    }
}
