package com.app.faketwitter.dto;

import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.constraints.*;

public class RegisterDTO {

    @NotBlank
    private String username;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotNull
    private MultipartFile photo;

    public RegisterDTO(String username, String email, String password, MultipartFile photo) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.photo = photo;
    }

    // Getters and Setters

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public MultipartFile getPhoto() {
        return photo;
    }

    public void setPhoto(MultipartFile photo) {
        this.photo = photo;
    }
}
