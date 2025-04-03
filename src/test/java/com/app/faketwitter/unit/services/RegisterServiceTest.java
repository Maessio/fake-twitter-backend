package com.app.faketwitter.services.unit;

import com.app.faketwitter.model.User;
import com.app.faketwitter.repository.UserRepository;
import com.app.faketwitter.service.RegisterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RegisterServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private RegisterService registerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testEmailExists_EmailExists() {
        String email = "test@example.com";
        when(userRepository.findByEmail(email)).thenReturn(new User());

        boolean exists = registerService.emailExists(email);

        assertTrue(exists);
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    public void testEmailExists_EmailDoesNotExist() {
        String email = "test@example.com";
        when(userRepository.findByEmail(email)).thenReturn(null);

        boolean exists = registerService.emailExists(email);

        assertFalse(exists);
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    public void testRegisterUser_Success() {
        String username = "testuser";
        String email = "test@example.com";
        String password = "password";
        String encodedPassword = "encodedPassword";

        when(userRepository.findByEmail(email)).thenReturn(null);
        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenReturn(new User());

        boolean result = registerService.registerUser(username, email, password);

        assertTrue(result);
        verify(userRepository, times(1)).findByEmail(email);
        verify(passwordEncoder, times(1)).encode(password);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testRegisterUser_EmailAlreadyInUse() {
        String username = "testuser";
        String email = "test@example.com";
        String password = "password";

        when(userRepository.findByEmail(email)).thenReturn(new User());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            registerService.registerUser(username, email, password);
        });

        assertEquals("Email already in use", exception.getMessage());
        verify(userRepository, times(1)).findByEmail(email);
        verify(userRepository, never()).save(any(User.class));
    }
}
