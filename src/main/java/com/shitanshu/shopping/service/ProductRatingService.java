package com.shitanshu.shopping.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shitanshu.shopping.model.OrderStatus;
import com.shitanshu.shopping.model.User;
import com.shitanshu.shopping.repository.OrderItemRepository;
import com.shitanshu.shopping.repository.ProductRatingRepository;
import com.shitanshu.shopping.repository.UserRepository;
import com.shitanshu.shopping.model.ProductRating;

@Service
public class ProductRatingService {

    @Autowired
    private ProductRatingRepository productRatingRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private UserRepository userRepository;


    public boolean canUserRateProduct(
            String email,
            Integer productId) {

        User user =
                userRepository.findByEmail(email)
                .orElseThrow(() ->
                    new RuntimeException(
                        "User not found"
                    )
                );


        return orderItemRepository
                .existsPurchasedProduct(
                        user.getId(),
                        productId,
                        OrderStatus.DELIVERED
                );
    }
    public ProductRating saveOrUpdateRating(
            String email,
            Integer productId,
            Double rating) {

        User user =
                userRepository.findByEmail(email)
                .orElseThrow(() ->
                    new RuntimeException("User not found")
                );
       


        boolean canRate =
                orderItemRepository
                        .existsPurchasedProduct(
                                user.getId(),
                                productId,
                                OrderStatus.DELIVERED
                        );


        if (!canRate) {

            throw new RuntimeException(
                    "You can rate only products you have purchased and received."
            );
        }


        ProductRating productRating =
                productRatingRepository
                        .findByProductIdAndUserId(
                                productId,
                                user.getId()
                        )
                        .orElse(null);


        if (productRating == null) {

            productRating =
                    new ProductRating();

            productRating.setProductId(productId);

            productRating.setUserId(user.getId());

        }


        productRating.setRating(rating);


        return productRatingRepository.save(
                productRating
        );
    }
    public ProductRating getMyRating(
            String email,
            Integer productId) {

        User user =
                userRepository.findByEmail(email)
                .orElseThrow(() ->
                    new RuntimeException(
                        "User not found"
                    )
                );

        return productRatingRepository
                .findByProductIdAndUserId(
                        productId,
                        user.getId()
                )
                .orElse(null);
    }
    public Double getProductAverageRating(
            Integer productId) {

        var ratings =
                productRatingRepository
                        .findByProductId(productId);


        // ======================
        // NO USER RATINGS
        // ======================

        if (ratings.isEmpty()) {

            return null;
        }


        // ======================
        // CALCULATE AVERAGE
        // ======================

        double totalRating = 0;

        for (ProductRating rating : ratings) {

            totalRating += rating.getRating();

        }


        return totalRating / ratings.size();

    }
}