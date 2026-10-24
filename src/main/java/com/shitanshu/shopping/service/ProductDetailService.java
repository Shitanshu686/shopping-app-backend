package com.shitanshu.shopping.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shitanshu.shopping.document.ProductDetailDocument;
import com.shitanshu.shopping.document.ProductDetailDocument.ProductReview;
import com.shitanshu.shopping.repository.mongo.ProductDetailMongoRepository;

@Service
public class ProductDetailService {

    @Autowired
    private ProductDetailMongoRepository mongoRepository;

    public ProductDetailDocument saveOrUpdateAttributes(Long productId, Map<String, Object> attributes) {
        Optional<ProductDetailDocument> existing = mongoRepository.findByProductId(productId);
        ProductDetailDocument doc;
        if (existing.isPresent()) {
            doc = existing.get();
            doc.setDynamicAttributes(attributes);
        } else {
            doc = ProductDetailDocument.builder()
                    .productId(productId)
                    .dynamicAttributes(attributes)
                    .reviews(new ArrayList<>())
                    .build();
        }
        return mongoRepository.save(doc);
    }

    public Optional<ProductDetailDocument> getByProductId(Long productId) {
        return mongoRepository.findByProductId(productId);
    }

    public ProductDetailDocument addReview(Long productId, Long userId, String userName, Integer rating, String comment) {
        ProductDetailDocument doc = mongoRepository.findByProductId(productId)
                .orElseGet(() -> ProductDetailDocument.builder()
                        .productId(productId)
                        .reviews(new ArrayList<>())
                        .build());

        if (doc.getReviews() == null) {
            doc.setReviews(new ArrayList<>());
        }

        ProductReview review = ProductReview.builder()
                .userId(userId)
                .userName(userName)
                .rating(rating)
                .comment(comment)
                .reviewDate(LocalDate.now().toString())
                .build();

        doc.getReviews().add(review);
        return mongoRepository.save(doc);
    }
}
