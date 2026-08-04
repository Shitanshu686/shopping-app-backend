package com.shitanshu.shopping.dto;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
public class ProductRequestDTO {

	@NotBlank(message = "Product name cannot be blank")
	private String name;

	@NotBlank(message = "Brand is required")
	private String brand;

	@NotBlank(message = "Description cannot be blank")
	private String description;

	@NotNull(message = "Price is required")
	@Positive(message = "Price must be greater than 0")
	private Double price;

	private Double oldPrice;

	@DecimalMin(value = "0.0", message = "Rating cannot be less than 0")
	@DecimalMax(value = "5.0", message = "Rating cannot be greater than 5")
	private Double rating;

	@NotBlank(message = "Image URL is required")
	private String image;

	@NotBlank(message = "Category is required")
	private String category;

	@NotNull(message = "Stock is required")
	@Min(value = 0, message = "Stock cannot be negative")
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