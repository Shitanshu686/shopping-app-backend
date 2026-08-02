package com.shitanshu.shopping.controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import java.util.List;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.shitanshu.shopping.dto.ProductResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import com.shitanshu.shopping.dto.ProductRequestDTO;


import com.shitanshu.shopping.model.Product;
import com.shitanshu.shopping.service.ProductService;
import org.springframework.web.bind.annotation.PutMapping;
@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getProducts() {

        return ResponseEntity.ok(productService.getAllProducts());

    }
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable int id) {

        return ResponseEntity.ok(productService.getProductById(id));

    }
    
    @PostMapping
    public ResponseEntity<ProductResponseDTO> addProduct(@Valid @RequestBody ProductRequestDTO productRequestDTO) {

    	ProductResponseDTO response = productService.addProduct(productRequestDTO);

    	return new ResponseEntity<>(response, HttpStatus.CREATED);

    }
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable int id,
    		@Valid @RequestBody  ProductRequestDTO productRequestDTO) {

    	ProductResponseDTO response = productService.updateProduct(id, productRequestDTO);

    	return ResponseEntity.ok(response);

    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable int id) {

        productService.deleteProduct(id);

        return ResponseEntity.ok("Product deleted successfully.");

    }
}