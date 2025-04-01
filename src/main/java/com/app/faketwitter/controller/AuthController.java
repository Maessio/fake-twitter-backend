package com.app.faketwitter.controller;

import com.app.faketwitter.model.User;
import com.app.faketwitter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {

        userService.registerUser(user.getEmail(), user.getPassword());

        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody User user) {

        userService.registerUser(user.getEmail(), user.getPassword());

        return ResponseEntity.ok("Login successful");
    }
}

