package com.shitanshu.shopping.repository.mongo;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.shitanshu.shopping.document.ProductDetailDocument;

@Repository
public interface ProductDetailMongoRepository extends MongoRepository<ProductDetailDocument, String> {

    Optional<ProductDetailDocument> findByProductId(Long productId);

    void deleteByProductId(Long productId);
}
