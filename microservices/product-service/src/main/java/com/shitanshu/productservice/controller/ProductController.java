package com.shitanshu.productservice.controller;

import com.shitanshu.productservice.dto.ProductRequestDTO;
import com.shitanshu.productservice.dto.ProductResponseDTO;
import com.shitanshu.productservice.service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@CrossOrigin
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public Page<ProductResponseDTO> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);

        return productService.getAllProducts(pageable);
    }

    @GetMapping("/{id}")
    public ProductResponseDTO getProductById(@PathVariable int id) {

        return productService.getProductById(id);
    }

    @GetMapping("/search")
    public Page<ProductResponseDTO> searchProducts(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);

        return productService.searchProducts(name, pageable);
    }

    @GetMapping("/category/{category}")
    public Page<ProductResponseDTO> filterByCategory(
            @PathVariable String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);

        return productService.filterByCategory(category, pageable);
    }

    @GetMapping("/brand/{brand}")
    public Page<ProductResponseDTO> filterByBrand(
            @PathVariable String brand,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);

        return productService.filterByBrand(brand, pageable);
    }

    @GetMapping("/price")
    public Page<ProductResponseDTO> filterByPrice(
            @RequestParam Double minPrice,
            @RequestParam Double maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);

        return productService.filterByPrice(minPrice, maxPrice, pageable);
    }

    @GetMapping("/rating")
    public Page<ProductResponseDTO> filterByRating(
            @RequestParam Double minRating,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);

        return productService.filterByRating(minRating, pageable);
    }

    @GetMapping("/filter")
    public Page<ProductResponseDTO> filterProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Double minRating,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);

        return productService.filterProducts(
                name,
                category,
                brand,
                minPrice,
                maxPrice,
                minRating,
                pageable
        );
    }

    @GetMapping("/{id}/similar")
    public List<ProductResponseDTO> getSimilarProducts(
            @PathVariable int id) {

        return productService.getSimilarProducts(id);
    }

    @PostMapping
    public ProductResponseDTO addProduct(
            @RequestBody ProductRequestDTO productRequestDTO) {

        return productService.addProduct(productRequestDTO);
    }

    @PutMapping("/{id}")
    public ProductResponseDTO updateProduct(
            @PathVariable int id,
            @RequestBody ProductRequestDTO productRequestDTO) {

        return productService.updateProduct(id, productRequestDTO);
    }

    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable int id) {

        productService.deleteProduct(id);

        return "Product deleted successfully";
    }
}
