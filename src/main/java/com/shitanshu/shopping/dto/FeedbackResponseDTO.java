package com.shitanshu.shopping.dto;

import java.time.LocalDateTime;

public class FeedbackResponseDTO {

    private Integer id;

    private Integer userId;

    private String userName;

    private String comment;

    private LocalDateTime createdAt;


    public FeedbackResponseDTO() {

    }


    public FeedbackResponseDTO(
            Integer id,
            Integer userId,
            String userName,
            String comment,
            LocalDateTime createdAt) {

        this.id = id;

        this.userId = userId;

        this.userName = userName;

        this.comment = comment;

        this.createdAt = createdAt;

    }


    public Integer getId() {

        return id;

    }

    public void setId(Integer id) {

        this.id = id;

    }


    public Integer getUserId() {

        return userId;

    }

    public void setUserId(Integer userId) {

        this.userId = userId;

    }


    public String getUserName() {

        return userName;

    }

    public void setUserName(String userName) {

        this.userName = userName;

    }


    public String getComment() {

        return comment;

    }

    public void setComment(String comment) {

        this.comment = comment;

    }


    public LocalDateTime getCreatedAt() {

        return createdAt;

    }

    public void setCreatedAt(LocalDateTime createdAt) {

        this.createdAt = createdAt;

    }

}