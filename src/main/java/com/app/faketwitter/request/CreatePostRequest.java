package com.app.faketwitter.request;

import jakarta.validation.constraints.NotBlank;

public class CreatePostRequest {

    @NotBlank(message = "Content is mandatory")
    private String content;

    // Getters and Setters
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}