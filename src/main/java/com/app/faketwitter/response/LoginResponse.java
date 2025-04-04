package com.app.faketwitter.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LoginResponse {

    @JsonProperty("token")
    private String token;

    @JsonProperty("userId")
    private Long userId;

    // Constructor
    public LoginResponse(Long userId, String token) {
        this.userId = userId;
        this.token = token;
    }
}
