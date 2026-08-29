package com.shitanshu.shopping.controller;
import java.time.LocalDateTime;
import com.shitanshu.shopping.response.ApiResponse;
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
import org.springframework.web.bind.annotation.RequestParam;


import com.shitanshu.shopping.model.Product;
import com.shitanshu.shopping.service.ProductService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ProductResponseDTO>>> getProducts(
            Pageable pageable
    ) {

        Page<ProductResponseDTO> products =
                productService.getAllProducts(pageable);

        ApiResponse<Page<ProductResponseDTO>> response =
                new ApiResponse<>(
                        true,
                        "Products fetched successfully",
                        products,
                        LocalDateTime.now()
                );

        return ResponseEntity.ok(response);

    }
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<ProductResponseDTO>>> searchProducts(

            @RequestParam String name,

            Pageable pageable

    ) {

        Page<ProductResponseDTO> products =
                productService.searchProducts(
                        name,
                        pageable
                );

        ApiResponse<Page<ProductResponseDTO>> response =
                new ApiResponse<>(
                        true,
                        "Products searched successfully",
                        products,
                        LocalDateTime.now()
                );

        return ResponseEntity.ok(response);
    }
    @GetMapping("/category")
    public ResponseEntity<ApiResponse<Page<ProductResponseDTO>>> filterByCategory(

            @RequestParam String category,

            Pageable pageable

    ) {

        Page<ProductResponseDTO> products =
                productService.filterByCategory(
                        category,
                        pageable
                );

        ApiResponse<Page<ProductResponseDTO>> response =
                new ApiResponse<>(
                        true,
                        "Products filtered by category successfully",
                        products,
                        LocalDateTime.now()
                );

        return ResponseEntity.ok(response);
    }
    @GetMapping("/brand")
    public ResponseEntity<ApiResponse<Page<ProductResponseDTO>>> filterByBrand(

            @RequestParam String brand,

            Pageable pageable

    ) {

        Page<ProductResponseDTO> products =
                productService.filterByBrand(
                        brand,
                        pageable
                );

        ApiResponse<Page<ProductResponseDTO>> response =
                new ApiResponse<>(
                        true,
                        "Products filtered by brand successfully",
                        products,
                        LocalDateTime.now()
                );

        return ResponseEntity.ok(response);
    }
    @GetMapping("/price")
    public ResponseEntity<ApiResponse<Page<ProductResponseDTO>>> filterByPrice(

            @RequestParam Double minPrice,

            @RequestParam Double maxPrice,

            Pageable pageable

    ) {

        Page<ProductResponseDTO> products =
                productService.filterByPrice(
                        minPrice,
                        maxPrice,
                        pageable
                );

        ApiResponse<Page<ProductResponseDTO>> response =
                new ApiResponse<>(
                        true,
                        "Products filtered by price successfully",
                        products,
                        LocalDateTime.now()
                );

        return ResponseEntity.ok(response);
    }
    @GetMapping("/rating")
    public ResponseEntity<ApiResponse<Page<ProductResponseDTO>>> filterByRating(

            @RequestParam Double minRating,

            Pageable pageable

    ) {

        Page<ProductResponseDTO> products =
                productService.filterByRating(
                        minRating,
                        pageable
                );

        ApiResponse<Page<ProductResponseDTO>> response =
                new ApiResponse<>(
                        true,
                        "Products filtered by rating successfully",
                        products,
                        LocalDateTime.now()
                );

        return ResponseEntity.ok(response);
    }
    @GetMapping("/filter")
    public ResponseEntity<ApiResponse<Page<ProductResponseDTO>>> filterProducts(

            @RequestParam(required = false) String name,

            @RequestParam(required = false) String category,

            @RequestParam(required = false) String brand,

            @RequestParam(required = false) Double minPrice,

            @RequestParam(required = false) Double maxPrice,

            @RequestParam(required = false) Double minRating,

            Pageable pageable

    ) {

        Page<ProductResponseDTO> products =
                productService.filterProducts(
                        name,
                        category,
                        brand,
                        minPrice,
                        maxPrice,
                        minRating,
                        pageable
                );

        ApiResponse<Page<ProductResponseDTO>> response =
                new ApiResponse<>(
                        true,
                        "Products filtered successfully",
                        products,
                        LocalDateTime.now()
                );

        return ResponseEntity.ok(response);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponseDTO>> getProductById(@PathVariable int id) {

        ProductResponseDTO product = productService.getProductById(id);

        ApiResponse<ProductResponseDTO> response =
                new ApiResponse<>(
                        true,
                        "Product fetched successfully",
                        product,
                        LocalDateTime.now());

        return ResponseEntity.ok(response);

    }
    @GetMapping("/{id}/similar")
    public ResponseEntity<ApiResponse<List<ProductResponseDTO>>>
    getSimilarProducts(@PathVariable int id) {

        List<ProductResponseDTO> products =
                productService.getSimilarProducts(id);

        ApiResponse<List<ProductResponseDTO>> response =
                new ApiResponse<>(
                        true,
                        "Similar products fetched successfully",
                        products,
                        LocalDateTime.now()
                );

        return ResponseEntity.ok(response);
    }
    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponseDTO>> addProduct(
            @Valid @RequestBody ProductRequestDTO productRequestDTO) {

        ProductResponseDTO product = productService.addProduct(productRequestDTO);

        ApiResponse<ProductResponseDTO> response =
                new ApiResponse<>(
                        true,
                        "Product created successfully",
                        product,
                        LocalDateTime.now());

        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponseDTO>> updateProduct(
            @PathVariable int id,
            @Valid @RequestBody ProductRequestDTO productRequestDTO) {

        ProductResponseDTO product = productService.updateProduct(id, productRequestDTO);

        ApiResponse<ProductResponseDTO> response =
                new ApiResponse<>(
                        true,
                        "Product updated successfully",
                        product,
                        LocalDateTime.now());

        return ResponseEntity.ok(response);

    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteProduct(@PathVariable int id) {

        productService.deleteProduct(id);

        ApiResponse<String> response =
                new ApiResponse<>(
                        true,
                        "Product deleted successfully",
                        null,
                        LocalDateTime.now());

        return ResponseEntity.ok(response);

    }
}