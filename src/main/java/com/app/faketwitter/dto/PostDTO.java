package com.app.faketwitter.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL) // Excludes null fields from serialization
public class PostDTO {

    private Long userId;
    private String userName;
    private String content;

    // Constructor
    public PostDTO(String content, Long userId, String userName) {
        this.content = content;
        this.userId = userId;
        this.userName = userName;
    }

    public PostDTO(String content, String userName) {
        this.content = content;
        this.userName = userName;
        this.userId = null; // userId será null caso não seja fornecido
    }

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
