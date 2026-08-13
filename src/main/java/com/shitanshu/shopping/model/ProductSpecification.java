package com.shitanshu.shopping.model;

import jakarta.persistence.*;

@Entity
@Table(name = "product_specifications")
public class ProductSpecification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String specificationName;

    private String specificationValue;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;


    public ProductSpecification() {
    }


    public ProductSpecification(
            Integer id,
            String specificationName,
            String specificationValue,
            Product product) {

        this.id = id;
        this.specificationName = specificationName;
        this.specificationValue = specificationValue;
        this.product = product;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getSpecificationName() {
        return specificationName;
    }

    public void setSpecificationName(String specificationName) {
        this.specificationName = specificationName;
    }


    public String getSpecificationValue() {
        return specificationValue;
    }

    public void setSpecificationValue(String specificationValue) {
        this.specificationValue = specificationValue;
    }


    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}