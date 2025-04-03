package com.app.faketwitter.utils;

import com.app.faketwitter.model.RevokedToken;
import com.app.faketwitter.repository.RevokedTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TokenUtils {

    @Autowired
    private RevokedTokenRepository revokedTokenRepository;

    public String recoverToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        return authHeader.replace("Bearer ", "");
    }

    public RevokedToken tokenRevoked(String token) {
        Optional<RevokedToken> revokedToken = revokedTokenRepository.findByToken(token);

        if (revokedToken.isPresent() && revokedToken.get().getToken().equals(token)) {
            return revokedToken.get();
        }

        return null;
    }

}
