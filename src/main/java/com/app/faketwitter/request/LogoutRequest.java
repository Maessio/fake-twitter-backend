package com.app.faketwitter.dto;

import jakarta.validation.constraints.NotBlank;

public class LogoutRequestDTO {

    @NotBlank(message = "Token is mandatory")
    private String token;

    // Getters and Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
