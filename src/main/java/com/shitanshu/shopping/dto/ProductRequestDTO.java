package com.shitanshu.shopping.dto;

public class ProductRequestDTO {

    private String name;

    private String brand;

    private String description;

    private Double price;

    private Double oldPrice;

    private Double rating;

    private String image;

    private String category;

    private Integer stock;
    public ProductRequestDTO() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getOldPrice() {
		return oldPrice;
	}

	public void setOldPrice(Double oldPrice) {
		this.oldPrice = oldPrice;
	}

	public Double getRating() {
		return rating;
	}

	public void setRating(Double rating) {
		this.rating = rating;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}
	

	public ProductRequestDTO(String name, String brand, String description, Double price, Double oldPrice,
			Double rating, String image, String category, Integer stock) {
		super();
		this.name = name;
		this.brand = brand;
		this.description = description;
		this.price = price;
		this.oldPrice = oldPrice;
		this.rating = rating;
		this.image = image;
		this.category = category;
		this.stock = stock;
	}

}