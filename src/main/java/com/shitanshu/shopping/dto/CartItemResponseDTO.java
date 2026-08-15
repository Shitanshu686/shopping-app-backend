package com.shitanshu.shopping.dto;

public class CartItemResponseDTO {

    private Integer id;
    private Integer productId;
    private String productName;
    private String image;
    private Double price;
    private Integer quantity;
    private Double subtotal;

    public CartItemResponseDTO() {
    }

    public CartItemResponseDTO(
            Integer id,
            Integer productId,
            String productName,
            String image,
            Double price,
            Integer quantity,
            Double subtotal) {

        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.image = image;
        this.price = price;
        this.quantity = quantity;
        this.subtotal = subtotal;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }
}