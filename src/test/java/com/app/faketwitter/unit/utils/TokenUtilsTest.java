package com.app.faketwitter.unit.utils;

import com.app.faketwitter.model.RevokedToken;
import com.app.faketwitter.repository.RevokedTokenRepository;
import com.app.faketwitter.utils.TokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TokenUtilsTest {

    @Mock
    private RevokedTokenRepository revokedTokenRepository;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private TokenUtils tokenUtils;

    @Test
    void shouldRecoverTokenFromRequest() {
        when(request.getHeader("Authorization")).thenReturn("Bearer valid_token");

        String token = tokenUtils.recoverToken(request);

        assertEquals("valid_token", token);
    }

    @Test
    void shouldReturnNullWhenAuthorizationHeaderIsMissing() {
        when(request.getHeader("Authorization")).thenReturn(null);

        String token = tokenUtils.recoverToken(request);

        assertNull(token);
    }

    @Test
    void shouldReturnNullWhenAuthorizationHeaderIsInvalid() {
        when(request.getHeader("Authorization")).thenReturn("InvalidHeader valid_token");

        String token = tokenUtils.recoverToken(request);

        assertNull(token);
    }

    @Test
    void shouldReturnRevokedTokenIfExists() {
        String token = "revoked_token";
        RevokedToken revokedToken = new RevokedToken();
        revokedToken.setToken(token);

        when(revokedTokenRepository.findByToken(token)).thenReturn(Optional.of(revokedToken));

        RevokedToken result = tokenUtils.tokenRevoked(token);

        assertNotNull(result);
        assertEquals(token, result.getToken());
    }

    @Test
    void shouldReturnNullIfTokenIsNotRevoked() {
        when(revokedTokenRepository.findByToken("valid_token")).thenReturn(Optional.empty());

        RevokedToken result = tokenUtils.tokenRevoked("valid_token");

        assertNull(result);
    }
}
