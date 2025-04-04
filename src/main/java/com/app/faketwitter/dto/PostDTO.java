package com.app.faketwitter.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL) // Excludes null fields from serialization
public class PostDTO {

    private Long id;
    private Long userId;
    private String username;
    private String content;

    // Constructor
    public PostDTO(String content, Long userId, String username) {
        this.id = null;
        this.content = content;
        this.userId = userId;
        this.username = username;
    }

    public PostDTO(Long id, String content, String username) {
        this.id = id;
        this.content = content;
        this.username = username;
        this.userId = null;
    }

    // Getters and Setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return username;
    }

    public void setUserName(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
