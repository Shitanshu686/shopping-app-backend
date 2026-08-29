package com.shitanshu.shopping.specification;

import com.shitanshu.shopping.model.Product;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecification {

    public static Specification<Product> hasName(String name) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")),
                        "%" + name.toLowerCase() + "%"
                );
    }
    public static Specification<Product> hasCategory(String category) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        criteriaBuilder.lower(root.get("category")),
                        category.toLowerCase()
                );
    }
    public static Specification<Product> hasBrand(String brand) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        criteriaBuilder.lower(root.get("brand")),
                        brand.toLowerCase()
                );
    }
    public static Specification<Product> priceBetween(
            Double minPrice,
            Double maxPrice
    ) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.between(
                        root.get("price"),
                        minPrice,
                        maxPrice
                );
    }
    public static Specification<Product> ratingGreaterThanEqual(
            Double minRating
    ) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(
                        root.get("rating"),
                        minRating
                );
    }
}