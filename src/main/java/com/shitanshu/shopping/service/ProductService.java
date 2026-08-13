package com.shitanshu.shopping.service;
import com.shitanshu.shopping.exception.ResourceAlreadyExistsException;
import com.shitanshu.shopping.dto.ProductRequestDTO;
import com.shitanshu.shopping.dto.ProductResponseDTO;
import com.shitanshu.shopping.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.ArrayList;
import java.util.List;
import com.shitanshu.shopping.exception.ProductNotFoundException;

import org.springframework.stereotype.Service;

import com.shitanshu.shopping.model.Product;

@Service
public class ProductService {
	
	@Autowired
	private ProductRepository productRepository;
	
	
	
	public List<ProductResponseDTO> getAllProducts() {



        
		List<Product> products = productRepository.findAll();
		List<ProductResponseDTO> responseList = new ArrayList<>();
		for (Product product : products) {
			ProductResponseDTO response = new ProductResponseDTO();
			response.setId(product.getId());
			response.setName(product.getName());
			response.setBrand(product.getBrand());
			response.setDescription(product.getDescription());
			response.setPrice(product.getPrice());
			response.setOldPrice(product.getOldPrice());
			response.setRating(product.getRating());
			response.setImage(product.getImage());
			response.setCategory(product.getCategory());
			response.setStock(product.getStock());
			
			responseList.add(response);
		}
		return responseList;
    }
    public ProductResponseDTO  getProductById(int id) {

    	Product product = productRepository.findById(id)
    	        .orElseThrow(() ->
    	                new ProductNotFoundException("Product with ID " + id + " not found"));

    	ProductResponseDTO response = new ProductResponseDTO();
    	response.setId(product.getId());
    	response.setName(product.getName());
    	response.setBrand(product.getBrand());
    	response.setDescription(product.getDescription());
    	response.setPrice(product.getPrice());
    	response.setOldPrice(product.getOldPrice());
    	response.setRating(product.getRating());
    	response.setImage(product.getImage());
    	response.setCategory(product.getCategory());
    	response.setStock(product.getStock());
    	
    	return response;

    }
    public List<ProductResponseDTO> getSimilarProducts(int id) {

        Product currentProduct =
                productRepository.findById(id)
                .orElseThrow(() ->
                    new ProductNotFoundException(
                        "Product with ID " + id + " not found"
                    )
                );

        List<Product> similarProducts =
                productRepository.findTop4ByCategoryAndIdNot(
                        currentProduct.getCategory(),
                        id
                );

        List<ProductResponseDTO> responseList =
                new ArrayList<>();

        for (Product product : similarProducts) {

            ProductResponseDTO response =
                    new ProductResponseDTO();

            response.setId(product.getId());
            response.setName(product.getName());
            response.setBrand(product.getBrand());
            response.setDescription(product.getDescription());
            response.setPrice(product.getPrice());
            response.setOldPrice(product.getOldPrice());
            response.setRating(product.getRating());
            response.setImage(product.getImage());
            response.setCategory(product.getCategory());
            response.setStock(product.getStock());

            responseList.add(response);
        }

        return responseList;
    }
    
    public ProductResponseDTO addProduct(ProductRequestDTO productRequestDTO){
    	if (productRepository.existsByName(productRequestDTO.getName())) {

    	    throw new ResourceAlreadyExistsException(
    	            "Product with name '" + productRequestDTO.getName() + "' already exists");

    	}
    	Product product = new Product();
    	product.setName(productRequestDTO.getName());
    	product.setBrand(productRequestDTO.getBrand());
    	product.setDescription(productRequestDTO.getDescription());
    	product.setPrice(productRequestDTO.getPrice());
    	product.setOldPrice(productRequestDTO.getOldPrice());
    	product.setRating(productRequestDTO.getRating());
    	product.setImage(productRequestDTO.getImage());
    	product.setCategory(productRequestDTO.getCategory());
    	product.setStock(productRequestDTO.getStock());
    	Product savedProduct = productRepository.save(product);
    	ProductResponseDTO response = new ProductResponseDTO();
    	response.setId(savedProduct.getId());
    	response.setName(savedProduct.getName());
    	response.setBrand(savedProduct.getBrand());
    	response.setDescription(savedProduct.getDescription());
    	response.setPrice(savedProduct.getPrice());
    	response.setOldPrice(savedProduct.getOldPrice());
    	response.setRating(savedProduct.getRating());
    	response.setImage(savedProduct.getImage());
    	response.setCategory(savedProduct.getCategory());
    	response.setStock(savedProduct.getStock());
    	return response;
    }
    
    public ProductResponseDTO updateProduct(int id, ProductRequestDTO productRequestDTO) {

        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException("Product with ID " + id + " not found"));

        existingProduct.setName(productRequestDTO.getName());
        existingProduct.setBrand(productRequestDTO.getBrand());
        existingProduct.setDescription(productRequestDTO.getDescription());
        existingProduct.setPrice(productRequestDTO.getPrice());
        existingProduct.setOldPrice(productRequestDTO.getOldPrice());
        existingProduct.setRating(productRequestDTO.getRating());
        existingProduct.setImage(productRequestDTO.getImage());
        existingProduct.setCategory(productRequestDTO.getCategory());
        existingProduct.setStock(productRequestDTO.getStock());

        Product savedProduct = productRepository.save(existingProduct);
        ProductResponseDTO response = new ProductResponseDTO();
        response.setId(savedProduct.getId());
        response.setName(savedProduct.getName());
        response.setBrand(savedProduct.getBrand());
        response.setDescription(savedProduct.getDescription());
        response.setPrice(savedProduct.getPrice());
        response.setOldPrice(savedProduct.getOldPrice());
        response.setRating(savedProduct.getRating());
        response.setImage(savedProduct.getImage());
        response.setCategory(savedProduct.getCategory());
        response.setStock(savedProduct.getStock());
        return response;
    }
    public void deleteProduct(int id) {

        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException("Product with ID " + id + " not found");
        }

        productRepository.deleteById(id);
    }
}