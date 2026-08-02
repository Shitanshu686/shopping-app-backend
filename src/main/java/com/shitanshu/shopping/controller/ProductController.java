package com.shitanshu.shopping.controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import java.util.List;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;

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
    public ResponseEntity<List<Product>> getProducts() {

        return ResponseEntity.ok(productService.getAllProducts());

    }
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable int id) {

        return ResponseEntity.ok(productService.getProductById(id));

    }
    
    @PostMapping
    public ResponseEntity<Product> addProduct(@Valid @RequestBody Product product) {

        Product savedProduct = productService.addProduct(product);

        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);

    }
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable int id,
    		@Valid @RequestBody Product product) {

        Product updatedProduct = productService.updateProduct(id, product);

        return ResponseEntity.ok(updatedProduct);

    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable int id) {

        productService.deleteProduct(id);

        return ResponseEntity.ok("Product deleted successfully.");

    }
}