package com.app.faketwitter.unit.controllers;

import com.app.faketwitter.controller.AuthController;
import com.app.faketwitter.dto.AuthenticationDTO;
import com.app.faketwitter.dto.ChangePasswordDTO;
import com.app.faketwitter.dto.RegisterDTO;
import com.app.faketwitter.response.ApiResponse;
import com.app.faketwitter.service.LoginService;
import com.app.faketwitter.service.RegisterService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private LoginService loginService;

    @Mock
    private RegisterService registerService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister_Success() {
        RegisterDTO registerDTO = new RegisterDTO("user", "user@example.com", "password");

        ResponseEntity<ApiResponse> response = authController.register(registerDTO);

        assertEquals(201, response.getStatusCode().value());
        assertTrue(((ApiResponse) response.getBody()).getMessage().contains("User registered successfully"));
    }

    @Test
    void testLogin_Failure() {
        AuthenticationDTO authDTO = new AuthenticationDTO("user@example.com", "wrongpassword");

        ResponseEntity<ApiResponse> response = authController.login(authDTO);

        assertEquals(400, response.getStatusCode().value());
        assertEquals("Invalid credentials", ((ApiResponse) response.getBody()).getMessage());
    }

    @Test
    void testLogout_Failure() throws Exception {
        ResponseEntity<ApiResponse> response = authController.logout(request);

        assertEquals(400, response.getStatusCode().value());
        assertEquals("Invalid credentials", response.getBody().getMessage());
    }

    @Test
    void testChangePassword_Success() throws Exception {
        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO("oldPass", "newPass");

        ResponseEntity<ApiResponse> response = authController.changePassword(1L, changePasswordDTO);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Password changed successfully", response.getBody().getMessage().trim());
    }

    @Test
    void testChangePassword_Failure() throws Exception {
        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO("oldPass", "newPass");

        ResponseEntity<ApiResponse> response = authController.changePassword(1L, changePasswordDTO);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Password changed successfully ", response.getBody().getMessage());
    }
}
