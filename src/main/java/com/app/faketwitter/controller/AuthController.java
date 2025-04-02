package com.app.faketwitter.controller;

import com.app.faketwitter.config.TokenService;
import com.app.faketwitter.dto.AuthenticationDTO;
import com.app.faketwitter.dto.ChangePasswordDTO;
import com.app.faketwitter.dto.RegisterDTO;
import com.app.faketwitter.model.User;
import com.app.faketwitter.repository.UserRepository;
import com.app.faketwitter.response.ApiResponse;
import com.app.faketwitter.response.LoginResponse;
import com.app.faketwitter.service.LoginService;
import com.app.faketwitter.service.RegisterService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private RegisterService registerService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository repository;

    @Autowired
    private TokenService tokenService;

    @PostMapping(value = "/register", consumes = "application/json", produces = "application/json")
    public ResponseEntity register(@RequestBody @Valid RegisterDTO data){

        try {
            registerService.registerUser(data.username(), data.email(), data.password());

            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(201, "User registered successfully", null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.error(409, e.getMessage()));
        }
    }

    @PostMapping(value = "/login", consumes = "application/json", produces = "application/json")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data) {

        try {
            var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
            var auth = this.authenticationManager.authenticate(usernamePassword);

            var token = tokenService.generateToken((User) auth.getPrincipal());

            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(200, "Login successful", new LoginResponse(token)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(400, "Invalid credentials"));
        }
    }

    @PostMapping("/logout/{userId}")
    public ResponseEntity<ApiResponse> logout(HttpServletRequest request) throws Exception {

        if (loginService.logout(request)){

            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(200, "Logout successful", null));
        } else {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(400, "Invalid credentials"));
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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(400, "Invalid credentials"));
        }
    }
}

