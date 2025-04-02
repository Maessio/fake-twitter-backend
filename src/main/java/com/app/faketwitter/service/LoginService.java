package com.app.faketwitter.service;

import com.app.faketwitter.model.RevokedToken;
import com.app.faketwitter.model.User;
import com.app.faketwitter.repository.RevokedTokenRepository;
import com.app.faketwitter.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class LoginService {


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RevokedTokenRepository revokedTokenRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public void logout(String token) throws Exception {
        try {
            RevokedToken revokedToken = new RevokedToken();
            revokedToken.setToken(token);

            revokedTokenRepository.save(revokedToken);

            // To clear the client authorization after logout
            SecurityContextHolder.clearContext();

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

