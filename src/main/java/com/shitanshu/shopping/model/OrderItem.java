package com.shitanshu.shopping.model;

import jakarta.persistence.*;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    // =========================
    // ORDER
    // =========================

    @ManyToOne
    @JoinColumn(
        name = "order_id",
        nullable = false
    )
    private Order order;


    // =========================
    // PRODUCT SNAPSHOT
    // =========================

    @Column(nullable = false)
    private Integer productId;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Double subtotal;


    // =========================
    // CONSTRUCTOR
    // =========================

    public OrderItem() {
    }


    public OrderItem(
            Integer id,
            Order order,
            Integer productId,
            String productName,
            Double price,
            Integer quantity,
            Double subtotal) {

        this.id = id;
        this.order = order;
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.subtotal = subtotal;
    }


    // =========================
    // GETTERS & SETTERS
    // =========================

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
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