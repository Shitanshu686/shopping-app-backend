package com.shitanshu.shopping.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shitanshu.shopping.dto.ProductSpecificationRequestDTO;
import com.shitanshu.shopping.dto.ProductSpecificationResponseDTO;
import com.shitanshu.shopping.exception.ProductNotFoundException;
import com.shitanshu.shopping.model.Product;
import com.shitanshu.shopping.model.ProductSpecification;
import com.shitanshu.shopping.repository.ProductRepository;
import com.shitanshu.shopping.repository.ProductSpecificationRepository;
import com.shitanshu.shopping.exception.ResourceAlreadyExistsException;
@Service
public class ProductSpecificationService {

    @Autowired
    private ProductSpecificationRepository productSpecificationRepository;

    @Autowired
    private ProductRepository productRepository;


    // =========================
    // ADD SPECIFICATION
    // =========================

    public ProductSpecificationResponseDTO addSpecification(
            int productId,
            ProductSpecificationRequestDTO request) {

        Product product =
                productRepository.findById(productId)
                .orElseThrow(() ->
                    new ProductNotFoundException(
                        "Product with ID " + productId + " not found"
                    )
                );
        // =========================
        // DUPLICATE CHECK
        // =========================

        if (productSpecificationRepository
                .existsByProductAndSpecificationName(
                        product,
                        request.getSpecificationName()
                )) {

            throw new ResourceAlreadyExistsException(
                    "Specification '" +
                    request.getSpecificationName() +
                    "' already exists for this product"
            );
        }


        ProductSpecification specification =
                new ProductSpecification();

        specification.setSpecificationName(
                request.getSpecificationName()
        );

        specification.setSpecificationValue(
                request.getSpecificationValue()
        );

        specification.setProduct(product);


        ProductSpecification savedSpecification =
                productSpecificationRepository.save(
                        specification
                );


        return convertToResponseDTO(
                savedSpecification
        );
    }


    // =========================
    // GET SPECIFICATIONS
    // =========================

    public List<ProductSpecificationResponseDTO>
    getSpecifications(int productId) {

        Product product =
                productRepository.findById(productId)
                .orElseThrow(() ->
                    new ProductNotFoundException(
                        "Product with ID " + productId + " not found"
                    )
                );


        List<ProductSpecification> specifications =
                productSpecificationRepository
                .findByProduct(product);


        List<ProductSpecificationResponseDTO> responseList =
                new ArrayList<>();


        for (ProductSpecification specification
                : specifications) {

            responseList.add(
                convertToResponseDTO(specification)
            );

        }


        return responseList;
    }


    // =========================
    // DTO MAPPING
    // =========================

    private ProductSpecificationResponseDTO
    convertToResponseDTO(
            ProductSpecification specification) {

        ProductSpecificationResponseDTO response =
                new ProductSpecificationResponseDTO();

        response.setId(
                specification.getId()
        );

        response.setSpecificationName(
                specification.getSpecificationName()
        );

        response.setSpecificationValue(
                specification.getSpecificationValue()
        );

        return response;
    }
}