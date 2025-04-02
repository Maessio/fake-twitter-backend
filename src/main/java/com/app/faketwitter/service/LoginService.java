package com.app.faketwitter.service;

import com.app.faketwitter.config.CustomUserDetailsService;
import com.app.faketwitter.model.RevokedToken;
import com.app.faketwitter.model.User;
import com.app.faketwitter.repository.RevokedTokenRepository;
import com.app.faketwitter.repository.UserRepository;
import com.app.faketwitter.utils.JwtTokenUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class LoginService {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private RevokedTokenRepository revokedTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public String login(String email, String password) throws Exception {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
        authenticationManager.authenticate(authenticationToken);

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
        return jwtTokenUtil.generateTokenFromUsername(userDetails);
    }

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

