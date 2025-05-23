package com.app.faketwitter.service;

import com.app.faketwitter.model.User;
import com.app.faketwitter.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
    public boolean registerUser(String username, String email, String password) {
        if (emailExists(email)) {
            throw new IllegalArgumentException("Email already in use");
        }

        User user = new User(username, email, passwordEncoder.encode(password));

        return  userRepository.save(user) != null;
    }

}

