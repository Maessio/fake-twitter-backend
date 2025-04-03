package com.app.faketwitter.services;

import com.app.faketwitter.model.RevokedToken;
import com.app.faketwitter.model.User;
import com.app.faketwitter.repository.RevokedTokenRepository;
import com.app.faketwitter.repository.UserRepository;
import com.app.faketwitter.service.LoginService;
import com.app.faketwitter.utils.TokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoginServiceTest {

    @Mock
    private TokenUtils tokenUtils;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RevokedTokenRepository revokedTokenRepository;

    @InjectMocks
    private LoginService loginService;

    private HttpServletRequest request;

    @BeforeEach
    public void setUp() {
        request = mock(HttpServletRequest.class);
    }

    @Test
    public void testLogout_Success() throws Exception {
        String token = "validToken";
        when(tokenUtils.recoverToken(request)).thenReturn(token);
        when(tokenUtils.tokenRevoked(token)).thenReturn(null);

        boolean result = loginService.logout(request);

        assertTrue(result);
        verify(revokedTokenRepository, times(1)).save(any(RevokedToken.class));
    }

    @Test
    public void testLogout_TokenAlreadyRevoked() throws Exception {
        String token = "revokedToken";
        when(tokenUtils.recoverToken(request)).thenReturn(token);
        when(tokenUtils.tokenRevoked(token)).thenReturn(new RevokedToken(token));

        boolean result = loginService.logout(request);

        assertTrue(result);
        verify(revokedTokenRepository, times(1)).save(any(RevokedToken.class));
    }

    @Test
    public void testLogout_NoToken() throws Exception {
        when(tokenUtils.recoverToken(request)).thenReturn("");

        boolean result = loginService.logout(request);

        assertFalse(result);
        verify(revokedTokenRepository, never()).save(any(RevokedToken.class));
    }

    @Test
    public void testChangePassword_Success() throws Exception {
        Long userId = 1L;
        String oldPassword = "oldPass";
        String newPassword = "newPass";
        User user = new User();
        user.setId(userId);
        user.setPassword("encodedOldPass");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(oldPassword, user.getPassword())).thenReturn(true);
        when(passwordEncoder.encode(newPassword)).thenReturn("encodedNewPass");

        loginService.changePassword(userId, oldPassword, newPassword);

        assertEquals("encodedNewPass", user.getPassword());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testChangePassword_UserNotFound() {
        Long userId = 1L;
        String oldPassword = "oldPass";
        String newPassword = "newPass";

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> {
            loginService.changePassword(userId, oldPassword, newPassword);
        });

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    public void testChangePassword_IncorrectOldPassword() {
        Long userId = 1L;
        String oldPassword = "wrongOldPass";
        String newPassword = "newPass";
        User user = new User();
        user.setId(userId);
        user.setPassword("encodedOldPass");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(oldPassword, user.getPassword())).thenReturn(false);

        Exception exception = assertThrows(Exception.class, () -> {
            loginService.changePassword(userId, oldPassword, newPassword);
        });

        assertEquals("Incorrect old password", exception.getMessage());
    }
}
