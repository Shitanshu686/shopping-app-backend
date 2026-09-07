package com.shitanshu.productservice.service;

import com.shitanshu.productservice.dto.ProductRequestDTO;
import com.shitanshu.productservice.dto.ProductResponseDTO;
import com.shitanshu.productservice.exception.ProductNotFoundException;
import com.shitanshu.productservice.exception.ResourceAlreadyExistsException;
import com.shitanshu.productservice.model.Product;
import com.shitanshu.productservice.repository.ProductRepository;
import com.shitanshu.productservice.specification.ProductSpecification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;


    // ==============================
    // GET ALL PRODUCTS
    // ==============================

    public Page<ProductResponseDTO> getAllProducts(Pageable pageable) {

        Page<Product> products =
                productRepository.findAll(pageable);

        return products.map(this::convertToResponse);
    }


    // ==============================
    // SEARCH PRODUCTS
    // ==============================

    public Page<ProductResponseDTO> searchProducts(
            String name,
            Pageable pageable) {

        Page<Product> products =
                productRepository.findByNameContainingIgnoreCase(
                        name,
                        pageable
                );

        return products.map(this::convertToResponse);
    }


    // ==============================
    // FILTER BY CATEGORY
    // ==============================

    public Page<ProductResponseDTO> filterByCategory(
            String category,
            Pageable pageable) {

        Page<Product> products =
                productRepository.findByCategoryIgnoreCase(
                        category,
                        pageable
                );

        return products.map(this::convertToResponse);
    }


    // ==============================
    // FILTER BY BRAND
    // ==============================

    public Page<ProductResponseDTO> filterByBrand(
            String brand,
            Pageable pageable) {

        Page<Product> products =
                productRepository.findByBrandIgnoreCase(
                        brand,
                        pageable
                );

        return products.map(this::convertToResponse);
    }


    // ==============================
    // FILTER BY PRICE
    // ==============================

    public Page<ProductResponseDTO> filterByPrice(
            Double minPrice,
            Double maxPrice,
            Pageable pageable) {

        Page<Product> products =
                productRepository.findByPriceBetween(
                        minPrice,
                        maxPrice,
                        pageable
                );

        return products.map(this::convertToResponse);
    }


    // ==============================
    // FILTER BY RATING
    // ==============================

    public Page<ProductResponseDTO> filterByRating(
            Double minRating,
            Pageable pageable) {

        Page<Product> products =
                productRepository.findByRatingGreaterThanEqual(
                        minRating,
                        pageable
                );

        return products.map(this::convertToResponse);
    }


    // ==============================
    // ADVANCED FILTER
    // ==============================

    public Page<ProductResponseDTO> filterProducts(
            String name,
            String category,
            String brand,
            Double minPrice,
            Double maxPrice,
            Double minRating,
            Pageable pageable) {

        Specification<Product> specification =
                (root, query, criteriaBuilder) -> null;

        if (name != null && !name.isBlank()) {

            specification =
                    specification.and(
                            ProductSpecification.hasName(name)
                    );
        }

        if (category != null && !category.isBlank()) {

            specification =
                    specification.and(
                            ProductSpecification.hasCategory(category)
                    );
        }

        if (brand != null && !brand.isBlank()) {

            specification =
                    specification.and(
                            ProductSpecification.hasBrand(brand)
                    );
        }

        if (minPrice != null && maxPrice != null) {

            specification =
                    specification.and(
                            ProductSpecification.priceBetween(
                                    minPrice,
                                    maxPrice
                            )
                    );
        }

        if (minRating != null) {

            specification =
                    specification.and(
                            ProductSpecification.ratingGreaterThanEqual(
                                    minRating
                            )
                    );
        }

        Page<Product> products =
                productRepository.findAll(
                        specification,
                        pageable
                );

        return products.map(this::convertToResponse);
    }


    // ==============================
    // GET PRODUCT BY ID
    // ==============================

    public ProductResponseDTO getProductById(int id) {

        Product product =
                productRepository.findById(id)
                        .orElseThrow(() ->
                                new ProductNotFoundException(
                                        "Product with ID " + id + " not found"
                                )
                        );

        return convertToResponse(product);
    }


    // ==============================
    // SIMILAR PRODUCTS
    // ==============================

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

            responseList.add(
                    convertToResponse(product)
            );
        }

        return responseList;
    }


    // ==============================
    // ADD PRODUCT
    // ==============================

    public ProductResponseDTO addProduct(
            ProductRequestDTO productRequestDTO) {

        if (productRepository.existsByName(
                productRequestDTO.getName())) {

            throw new ResourceAlreadyExistsException(
                    "Product with name '"
                    + productRequestDTO.getName()
                    + "' already exists"
            );
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

        Product savedProduct =
                productRepository.save(product);

        return convertToResponse(savedProduct);
    }


    // ==============================
    // UPDATE PRODUCT
    // ==============================

    public ProductResponseDTO updateProduct(
            int id,
            ProductRequestDTO productRequestDTO) {

        Product existingProduct =
                productRepository.findById(id)
                        .orElseThrow(() ->
                                new ProductNotFoundException(
                                        "Product with ID "
                                        + id
                                        + " not found"
                                )
                        );

        existingProduct.setName(productRequestDTO.getName());
        existingProduct.setBrand(productRequestDTO.getBrand());
        existingProduct.setDescription(productRequestDTO.getDescription());
        existingProduct.setPrice(productRequestDTO.getPrice());
        existingProduct.setOldPrice(productRequestDTO.getOldPrice());
        existingProduct.setRating(productRequestDTO.getRating());
        existingProduct.setImage(productRequestDTO.getImage());
        existingProduct.setCategory(productRequestDTO.getCategory());
        existingProduct.setStock(productRequestDTO.getStock());

        Product savedProduct =
                productRepository.save(existingProduct);

        return convertToResponse(savedProduct);
    }


    // ==============================
    // DELETE PRODUCT
    // ==============================

    public void deleteProduct(int id) {

        if (!productRepository.existsById(id)) {

            throw new ProductNotFoundException(
                    "Product with ID " + id + " not found"
            );
        }

        productRepository.deleteById(id);
    }


    // ==============================
    // ENTITY → RESPONSE DTO
    // ==============================

    private ProductResponseDTO convertToResponse(
            Product product) {

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

        return response;
    }
}
