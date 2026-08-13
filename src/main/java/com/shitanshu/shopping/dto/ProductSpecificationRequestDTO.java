package com.shitanshu.shopping.dto;

import jakarta.validation.constraints.NotBlank;

public class ProductSpecificationRequestDTO {

    @NotBlank(message = "Specification name is required")
    private String specificationName;

    @NotBlank(message = "Specification value is required")
    private String specificationValue;


    public ProductSpecificationRequestDTO() {
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