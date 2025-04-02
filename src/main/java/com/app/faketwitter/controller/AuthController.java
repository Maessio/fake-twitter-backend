package com.app.faketwitter.controller;

import com.app.faketwitter.dto.ChangePasswordDTO;
import com.app.faketwitter.request.LoginRequest;
import com.app.faketwitter.request.RegisterRequest;
import com.app.faketwitter.response.ApiResponse;
import com.app.faketwitter.response.LoginResponse;
import com.app.faketwitter.service.LoginService;
import com.app.faketwitter.service.RegisterService;
import com.app.faketwitter.service.UserService;
import com.app.faketwitter.utils.JwtTokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private LoginService loginService;

    @Autowired
    private RegisterService registerService;

    @PostMapping(value = "/register", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ApiResponse> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {

        try {
            registerService.registerUser(registerRequest.getUsername(), registerRequest.getEmail(), registerRequest.getPassword());

            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(201, "User registered successfully", null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.error(409, e.getMessage()));
        }
    }

    @PostMapping(value = "/login", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ApiResponse> loginUser(@Valid @RequestBody LoginRequest loginRequest) {

        try {
            String jwtToken = loginService.login(loginRequest.getEmail(), loginRequest.getPassword());

            return ResponseEntity.ok(ApiResponse.success(200, "Login successful" , new LoginResponse(jwtToken)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(400, "Invalid credentials"));
        }
    }

    @PostMapping(value = "/logout", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ApiResponse> logout(@RequestHeader(value = "Authorization", required = false) HttpServletRequest authorizationHeader) {

        try {
            loginService.logout();

            return ResponseEntity.ok(ApiResponse.success(200, "Logout successful ", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error(500, "Internal Server Error"));
        }
    }

    @PutMapping("/{userId}/change-password")
    public ResponseEntity<ApiResponse> changePassword(
            @PathVariable Long userId,
            @RequestBody ChangePasswordDTO changePasswordDTO) {
        try {
            loginService.changePassword(userId, changePasswordDTO.getOldPassword(), changePasswordDTO.getNewPassword());
            return ResponseEntity.ok(ApiResponse.success(200, "Password changed successfully ", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error(500, "Internal Server Error"));
        }
    }
}

