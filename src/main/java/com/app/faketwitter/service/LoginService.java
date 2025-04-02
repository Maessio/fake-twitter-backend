package com.app.faketwitter.service;

import com.app.faketwitter.TokenUtils;
import com.app.faketwitter.model.RevokedToken;
import com.app.faketwitter.model.User;
import com.app.faketwitter.repository.RevokedTokenRepository;
import com.app.faketwitter.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RevokedTokenRepository revokedTokenRepository;

    @Transactional
    public boolean logout( HttpServletRequest request) throws Exception {
        try {
            String token = tokenUtils.recoverToken(request);

            if (token.isBlank()) {
                return false;
            }

            RevokedToken revokedToken = tokenUtils.tokenRevoked(token);

            if (revokedToken == null) {
                revokedTokenRepository.save(new RevokedToken(token));

                return true;
            } else {
                revokedTokenRepository.save(revokedToken);

                return true;
            }
        } catch (Exception e) {
            throw new Exception("Erro inesperado ao revogar o token", e);
        }
    }

    @Transactional
    public void changePassword(Long userId, String oldPassword, String newPassword) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("User not found"));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new Exception("Incorrect old password");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

}

