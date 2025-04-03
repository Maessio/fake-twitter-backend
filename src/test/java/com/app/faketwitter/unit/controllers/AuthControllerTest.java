package com.app.faketwitter.controllers;

import com.app.faketwitter.config.TokenService;
import com.app.faketwitter.controller.AuthController;
import com.app.faketwitter.dto.AuthenticationDTO;
import com.app.faketwitter.dto.ChangePasswordDTO;
import com.app.faketwitter.dto.RegisterDTO;
import com.app.faketwitter.response.ApiResponse;
import com.app.faketwitter.response.LoginResponse;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

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
    private TokenService tokenService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister_Success() {
        RegisterDTO registerDTO = new RegisterDTO("user", "user@example.com", "password");

        ResponseEntity response = authController.register(registerDTO);

        assertEquals(201, response.getStatusCode().value());
        assertTrue(((ApiResponse) response.getBody()).getMessage().contains("User registered successfully"));
    }

    @Test
    void testRegister_Conflict() {
        RegisterDTO registerDTO = new RegisterDTO("user", "user@example.com", "password");
        doThrow(new IllegalArgumentException("User already exists")).when(registerService).registerUser(any(), any(), any());

        ResponseEntity response = authController.register(registerDTO);

        assertEquals(409, response.getStatusCode().value());
        assertTrue(((ApiResponse) response.getBody()).getMessage().contains("User already exists"));
    }

    @Test
    void testLogin_Success() {
        AuthenticationDTO authDTO = new AuthenticationDTO("user@example.com", "password");
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(tokenService.generateToken(any())).thenReturn("fakeToken");

        ResponseEntity response = authController.login(authDTO);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Login successful", ((ApiResponse) response.getBody()).getMessage());
        assertEquals("fakeToken", ((LoginResponse) ((ApiResponse) response.getBody()).getData()).getToken());
    }

    @Test
    void testLogin_Failure() {
        AuthenticationDTO authDTO = new AuthenticationDTO("user@example.com", "wrongpassword");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(new RuntimeException());

        ResponseEntity response = authController.login(authDTO);

        assertEquals(400, response.getStatusCode().value());
        assertEquals("Invalid credentials", ((ApiResponse) response.getBody()).getMessage());
    }

    @Test
    void testLogout_Success() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(loginService.logout(request)).thenReturn(true);

        ResponseEntity<ApiResponse> response = authController.logout(request);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Logout successful", response.getBody().getMessage());
    }

    @Test
    void testLogout_Failure() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(loginService.logout(request)).thenReturn(false);

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
        doThrow(new RuntimeException("Invalid credentials"))
                .when(loginService).changePassword(anyLong(), any(), any());

        ResponseEntity<ApiResponse> response = authController.changePassword(1L, changePasswordDTO);

        assertEquals(400, response.getStatusCode().value());
        assertEquals("Invalid credentials", response.getBody().getMessage());
    }
}
