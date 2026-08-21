package com.shitanshu.shopping.controller;

import com.shitanshu.shopping.dto.FeedbackRequestDTO;
import com.shitanshu.shopping.dto.FeedbackResponseDTO;
import com.shitanshu.shopping.response.ApiResponse;
import com.shitanshu.shopping.service.FeedbackService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/feedback")
@CrossOrigin(origins = "*")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;


    // ======================
    // GET PRODUCT FEEDBACK
    // PUBLIC API
    // ======================

    @GetMapping("/products/{productId}")
    public ResponseEntity<ApiResponse<List<FeedbackResponseDTO>>>
            getProductFeedback(
                    @PathVariable Integer productId) {

        List<FeedbackResponseDTO> feedback =
                feedbackService.getProductFeedback(
                        productId
                );

        ApiResponse<List<FeedbackResponseDTO>> response =
                new ApiResponse<>(
                        true,
                        "Feedback fetched successfully",
                        feedback,
                        LocalDateTime.now()
                );

        return ResponseEntity.ok(response);
    }


    // ======================
    // ADD FEEDBACK
    // LOGIN REQUIRED
    // ======================

    @PostMapping("/products/{productId}")
    public ResponseEntity<ApiResponse<FeedbackResponseDTO>>
            addFeedback(

                    @PathVariable Integer productId,

                    @Valid
                    @RequestBody
                    FeedbackRequestDTO request,

                    Authentication authentication) {

        String email =
                authentication.getName();


        FeedbackResponseDTO feedback =
                feedbackService.addFeedback(
                        productId,
                        email,
                        request
                );


        ApiResponse<FeedbackResponseDTO> response =
                new ApiResponse<>(
                        true,
                        "Feedback submitted successfully",
                        feedback,
                        LocalDateTime.now()
                );

        return ResponseEntity.ok(response);
    }


    // ======================
    // UPDATE FEEDBACK
    // LOGIN REQUIRED
    // ======================

    @PutMapping("/{feedbackId}")
    public ResponseEntity<ApiResponse<FeedbackResponseDTO>>
            updateFeedback(

                    @PathVariable Integer feedbackId,

                    @Valid
                    @RequestBody
                    FeedbackRequestDTO request,

                    Authentication authentication) {

        String email =
                authentication.getName();


        FeedbackResponseDTO feedback =
                feedbackService.updateFeedback(
                        feedbackId,
                        email,
                        request
                );


        ApiResponse<FeedbackResponseDTO> response =
                new ApiResponse<>(
                        true,
                        "Feedback updated successfully",
                        feedback,
                        LocalDateTime.now()
                );

        return ResponseEntity.ok(response);
    }


    // ======================
    // DELETE FEEDBACK
    // LOGIN REQUIRED
    // ======================

    @DeleteMapping("/{feedbackId}")
    public ResponseEntity<ApiResponse<String>>
            deleteFeedback(

                    @PathVariable Integer feedbackId,

                    Authentication authentication) {

        String email =
                authentication.getName();


        feedbackService.deleteFeedback(
                feedbackId,
                email
        );


        ApiResponse<String> response =
                new ApiResponse<>(
                        true,
                        "Feedback deleted successfully",
                        null,
                        LocalDateTime.now()
                );

        return ResponseEntity.ok(response);
    }

}