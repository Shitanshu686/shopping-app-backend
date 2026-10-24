package com.shitanshu.shopping.document;

import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "product_details")
public class ProductDetailDocument {

    @Id
    private String id;

    // Foreign correlation key referencing MySQL product.id
    @Indexed(unique = true)
    private Long productId;

    // Polymorphic dynamic specifications (RAM, Battery, Dimensions, Fabric, etc.)
    private Map<String, Object> dynamicAttributes;

    // Rich embedded customer reviews without joins
    private List<ProductReview> reviews;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductReview {
        private Long userId;
        private String userName;
        private Integer rating;
        private String comment;
        private String reviewDate;
    }
}
