package com.shitanshu.shopping.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class FeedbackRequestDTO {

    @NotBlank(message = "Feedback cannot be empty")
    @Size(
        min = 3,
        max = 1000,
        message = "Feedback must be between 3 and 1000 characters"
    )
    private String comment;


    public FeedbackRequestDTO() {

    }


    public String getComment() {
        return comment;
    }


    public void setComment(String comment) {
        this.comment = comment;
    }
}