package com.shitanshu.shopping.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public class ProductRatingRequestDTO {

    @NotNull(message = "Product ID is required")
    private Integer productId;

    @NotNull(message = "Rating is required")
    @DecimalMin(value = "1.0", message = "Rating must be at least 1")
    @DecimalMax(value = "5.0", message = "Rating cannot be greater than 5")
    private Double rating;


    public ProductRatingRequestDTO() {
    }


    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }


    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

}