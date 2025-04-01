package com.app.faketwitter.controller;

import com.app.faketwitter.dto.LoginRequestDTO;
import com.app.faketwitter.dto.RegisterRequestDTO;
import com.app.faketwitter.response.ApiResponse;
import com.app.faketwitter.service.LoginService;
import com.app.faketwitter.service.RegisterService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private RegisterService registerService;

    @PostMapping(value = "/register", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ApiResponse> registerUser(@Valid @RequestBody RegisterRequestDTO registerRequestDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder("Validation failed: ");
            bindingResult.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()).append(", "));

            return ResponseEntity.badRequest().body(ApiResponse.error(400, errorMessage.toString()));
        }

        registerService.registerUser(registerRequestDTO.getUsername(), registerRequestDTO.getEmail(), registerRequestDTO.getPassword());

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(201, "User registered successfully", null));
    }

    @PostMapping(value = "/login", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ApiResponse> loginUser(@RequestBody LoginRequestDTO loginRequestDTO) {

        try {
            String jwtToken = loginService.login(loginRequestDTO.getEmail(), loginRequestDTO.getPassword());
            return ResponseEntity.ok(ApiResponse.success(200, "Login successful, token: " + jwtToken, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error(500, "Invalid credentials"));
        }
    }
}

