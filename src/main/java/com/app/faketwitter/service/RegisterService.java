package com.app.faketwitter.service;

import com.app.faketwitter.model.User;
import com.app.faketwitter.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class RegisterService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public boolean emailExists(String email) {
        UserDetails user = userRepository.findByEmail(email);

        return user != null;
    }

    @Transactional
    public boolean registerUser(String username, String email, String password, MultipartFile photo) throws IOException {
        if (emailExists(email)) {
            throw new IllegalArgumentException("Email already in use");
        }

        User user = new User(username, email, passwordEncoder.encode(password), photo.getBytes());

        return  userRepository.save(user) != null;
    }

}

