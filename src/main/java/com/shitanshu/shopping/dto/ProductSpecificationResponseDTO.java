package com.shitanshu.shopping.dto;

public class ProductSpecificationResponseDTO {

    private Integer id;

    private String specificationName;

    private String specificationValue;


    public ProductSpecificationResponseDTO() {
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
}