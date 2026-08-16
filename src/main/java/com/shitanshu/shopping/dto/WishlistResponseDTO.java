package com.shitanshu.shopping.dto;

import java.util.List;

public class WishlistResponseDTO {

    private Integer wishlistId;

    private List<WishlistItemResponseDTO> items;

    private Integer totalItems;


    // =========================
    // GETTERS AND SETTERS
    // =========================

    public Integer getWishlistId() {
        return wishlistId;
    }

    public void setWishlistId(Integer wishlistId) {
        this.wishlistId = wishlistId;
    }

    public List<WishlistItemResponseDTO> getItems() {
        return items;
    }

    public void setItems(List<WishlistItemResponseDTO> items) {
        this.items = items;
    }

    public Integer getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(Integer totalItems) {
        this.totalItems = totalItems;
    }
}