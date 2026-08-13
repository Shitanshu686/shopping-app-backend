package com.shitanshu.shopping.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.shitanshu.shopping.dto.ProductSpecificationRequestDTO;
import com.shitanshu.shopping.dto.ProductSpecificationResponseDTO;
import com.shitanshu.shopping.response.ApiResponse;
import com.shitanshu.shopping.service.ProductSpecificationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/products")
public class ProductSpecificationController {

    @Autowired
    private ProductSpecificationService productSpecificationService;


    // =========================
    // ADD SPECIFICATION
    // =========================

    @PostMapping("/{productId}/specifications")
    public ResponseEntity<ApiResponse<ProductSpecificationResponseDTO>>
    addSpecification(
            @PathVariable int productId,
            @Valid @RequestBody ProductSpecificationRequestDTO request) {

        ProductSpecificationResponseDTO specification =
                productSpecificationService.addSpecification(
                        productId,
                        request
                );

        ApiResponse<ProductSpecificationResponseDTO> response =
                new ApiResponse<>(
                        true,
                        "Product specification added successfully",
                        specification,
                        LocalDateTime.now()
                );

        return new ResponseEntity<>(
                response,
                HttpStatus.CREATED
        );
    }


    // =========================
    // GET SPECIFICATIONS
    // =========================

    @GetMapping("/{productId}/specifications")
    public ResponseEntity<ApiResponse<List<ProductSpecificationResponseDTO>>>
    getSpecifications(
            @PathVariable int productId) {

        List<ProductSpecificationResponseDTO> specifications =
                productSpecificationService
                .getSpecifications(productId);

        ApiResponse<List<ProductSpecificationResponseDTO>> response =
                new ApiResponse<>(
                        true,
                        "Product specifications fetched successfully",
                        specifications,
                        LocalDateTime.now()
                );

        return ResponseEntity.ok(response);
    }
}