package com.shitanshu.shopping.service;

import com.shitanshu.shopping.dto.FeedbackRequestDTO;
import com.shitanshu.shopping.dto.FeedbackResponseDTO;
import com.shitanshu.shopping.exception.BadRequestException;
import com.shitanshu.shopping.exception.InvalidCredentialsException;
import com.shitanshu.shopping.model.Feedback;
import com.shitanshu.shopping.model.Product;
import com.shitanshu.shopping.model.User;
import com.shitanshu.shopping.repository.FeedbackRepository;
import com.shitanshu.shopping.repository.ProductRepository;
import com.shitanshu.shopping.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;


    // =========================================================
    // GET PRODUCT FEEDBACK
    // =========================================================

    public List<FeedbackResponseDTO> getProductFeedback(
            Integer productId) {

        Product product =
                productRepository.findById(productId)
                .orElseThrow(() ->
                    new BadRequestException(
                        "Product not found"
                    )
                );

        List<Feedback> feedbackList =
                feedbackRepository.findByProduct(product);

        return feedbackList
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }


    // =========================================================
    // ADD FEEDBACK
    // =========================================================

    public FeedbackResponseDTO addFeedback(
            Integer productId,
            String email,
            FeedbackRequestDTO request) {

        // ======================
        // FIND USER
        // ======================

        User user =
                userRepository.findByEmail(email)
                .orElseThrow(() ->
                    new InvalidCredentialsException(
                        "User not found"
                    )
                );


        // ======================
        // FIND PRODUCT
        // ======================

        Product product =
                productRepository.findById(productId)
                .orElseThrow(() ->
                    new BadRequestException(
                        "Product not found"
                    )
                );


        // ======================
        // CHECK DUPLICATE
        // ======================

        if (feedbackRepository
                .existsByUserAndProduct(user, product)) {

            throw new BadRequestException(
                "You have already submitted feedback for this product"
            );
        }


        // ======================
        // CREATE FEEDBACK
        // ======================

        Feedback feedback =
                new Feedback();

        feedback.setComment(
                request.getComment()
        );

        feedback.setUser(user);

        feedback.setProduct(product);

        feedback.setCreatedAt(
                LocalDateTime.now()
        );


        // ======================
        // SAVE
        // ======================

        Feedback savedFeedback =
                feedbackRepository.save(feedback);


        // ======================
        // RESPONSE
        // ======================

        return convertToResponseDTO(
                savedFeedback
        );
    }


    // =========================================================
    // UPDATE FEEDBACK
    // =========================================================

    public FeedbackResponseDTO updateFeedback(
            Integer feedbackId,
            String email,
            FeedbackRequestDTO request) {

        // ======================
        // FIND USER
        // ======================

        User user =
                userRepository.findByEmail(email)
                .orElseThrow(() ->
                    new InvalidCredentialsException(
                        "User not found"
                    )
                );


        // ======================
        // FIND FEEDBACK
        // ======================

        Feedback feedback =
                feedbackRepository.findById(feedbackId)
                .orElseThrow(() ->
                    new BadRequestException(
                        "Feedback not found"
                    )
                );


        // ======================
        // OWNER CHECK
        // ======================

        if (!feedback.getUser().getId()
                .equals(user.getId())) {

            throw new BadRequestException(
                "You can only update your own feedback"
            );
        }


        // ======================
        // UPDATE COMMENT
        // ======================

        feedback.setComment(
                request.getComment()
        );


        // ======================
        // SAVE
        // ======================

        Feedback updatedFeedback =
                feedbackRepository.save(feedback);


        // ======================
        // RESPONSE
        // ======================

        return convertToResponseDTO(
                updatedFeedback
        );
    }


    // =========================================================
    // DELETE FEEDBACK
    // =========================================================

    public void deleteFeedback(
            Integer feedbackId,
            String email) {

        // ======================
        // FIND USER
        // ======================

        User user =
                userRepository.findByEmail(email)
                .orElseThrow(() ->
                    new InvalidCredentialsException(
                        "User not found"
                    )
                );


        // ======================
        // FIND FEEDBACK
        // ======================

        Feedback feedback =
                feedbackRepository.findById(feedbackId)
                .orElseThrow(() ->
                    new BadRequestException(
                        "Feedback not found"
                    )
                );


        // ======================
        // ADMIN / OWNER CHECK
        // ======================

        boolean isAdmin =
                "ADMIN".equalsIgnoreCase(
                    user.getRole()
                );

        boolean isOwner =
                feedback.getUser().getId()
                        .equals(user.getId());


        if (!isAdmin && !isOwner) {

            throw new BadRequestException(
                "You can only delete your own feedback"
            );
        }


        // ======================
        // DELETE
        // ======================

        feedbackRepository.delete(feedback);
    }


    // =========================================================
    // DTO MAPPING
    // =========================================================

    private FeedbackResponseDTO convertToResponseDTO(
            Feedback feedback) {

        return new FeedbackResponseDTO(

                feedback.getId(),

                feedback.getUser().getId(),

                feedback.getUser().getName(),

                feedback.getComment(),

                feedback.getCreatedAt()

        );
    }

}