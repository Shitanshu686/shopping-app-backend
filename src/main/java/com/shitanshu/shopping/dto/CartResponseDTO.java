package com.shitanshu.shopping.dto;

import java.util.List;

public class CartResponseDTO {

    private Integer cartId;
    private List<CartItemResponseDTO> items;
    private Double total;
    private Integer totalItems;

    public CartResponseDTO() {
    }

    public CartResponseDTO(
            Integer cartId,
            List<CartItemResponseDTO> items,
            Double total,
            Integer totalItems) {

        this.cartId = cartId;
        this.items = items;
        this.total = total;
        this.totalItems = totalItems;
    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public List<CartItemResponseDTO> getItems() {
        return items;
    }

    public void setItems(List<CartItemResponseDTO> items) {
        this.items = items;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Integer getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(Integer totalItems) {
        this.totalItems = totalItems;
    }
}