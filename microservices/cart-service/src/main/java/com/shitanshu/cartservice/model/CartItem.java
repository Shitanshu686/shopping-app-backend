package com.shitanshu.cartservice.model;

import jakarta.persistence.*;

@Entity
@Table(name = "cart_items")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    // =========================
    // FLASH SALE DETAILS
    // =========================

    @Column(name = "original_price")
    private Double originalPrice;

    @Column(name = "sale_price")
    private Double salePrice;

    @Column(name = "discount_percent")
    private Integer discountPercent;

    @Column(name = "flash_sale")
    private boolean flashSale;

    public CartItem() {
    }

    public CartItem(
            Integer id,
            Cart cart,
            Product product,
            Integer quantity,
            Double originalPrice,
            Double salePrice,
            Integer discountPercent,
            boolean flashSale) {

        this.id = id;
        this.cart = cart;
        this.product = product;
        this.quantity = quantity;
        this.originalPrice = originalPrice;
        this.salePrice = salePrice;
        this.discountPercent = discountPercent;
        this.flashSale = flashSale;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(Double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public Double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(Double salePrice) {
        this.salePrice = salePrice;
    }

    public Integer getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(Integer discountPercent) {
        this.discountPercent = discountPercent;
    }

    public boolean isFlashSale() {
        return flashSale;
    }

    public void setFlashSale(boolean flashSale) {
        this.flashSale = flashSale;
    }
}
