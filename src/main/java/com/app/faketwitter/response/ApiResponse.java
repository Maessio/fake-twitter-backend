package com.app.faketwitter.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL) // Excludes null fields from serialization
public class ApiResponse {

    @JsonProperty("statusCode")
    private final int statusCode;

    @JsonProperty("message")
    private final String message;

    @JsonProperty("data")
    private final Object data;

    //Constructor
    public ApiResponse(int statusCode, String message, Object data) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }

    // Methods
    public static ApiResponse success(int statusCode, String message, Object data) {
        return new ApiResponse(statusCode, message, data);
    }

    public static ApiResponse error(int statusCode, String message) {
        return new ApiResponse(statusCode, message, null);
    }
}
