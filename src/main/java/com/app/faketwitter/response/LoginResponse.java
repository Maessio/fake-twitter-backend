package com.app.faketwitter.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LoginResponse {

    @JsonProperty("token")
    private String token;

    // Constructor
    public LoginResponse(String token) {
        this.token = token;
    }
}
