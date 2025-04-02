package com.app.faketwitter.controller;

import com.app.faketwitter.dto.PostDTO;
import com.app.faketwitter.dto.UserDTO;
import com.app.faketwitter.model.User;
import com.app.faketwitter.request.CreatePostRequest;
import com.app.faketwitter.response.ApiResponse;
import com.app.faketwitter.service.UserService;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse> getUserProfile(@PathVariable Long userId) {
        try {
            UserDTO user = userService.getUserProfile(userId);
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(200, "Profile found", user));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error(500, "Internal Server Error"));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse> searchUsers(@RequestParam String userName) {

        try {
            List<UserDTO> users = userService.searchUsers(userName);

            if (users.isEmpty()){
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponse.success(200, "Profile not found", null));
            }

            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(200, "Profile found", users));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error(500, "Internal Server Error"));

        }
    }

    @PostMapping("/{userId}/follow/{followId}")
    public ResponseEntity<ApiResponse> followUser(@PathVariable Long userId, @PathVariable Long followId) {
        try {
            userService.followUser(userId, followId);
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(201, "User followed successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error(500, "Internal Server Error"));
        }
    }

    @PostMapping("/{userId}/posts")
    public ResponseEntity<ApiResponse> createPost(@PathVariable Long userId, @Valid @RequestBody CreatePostRequest content) {
        try {
            userService.createPost(userId, content.getContent());
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(201, "Post created successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error(500, "Internal Server Error"));
        }
    }

    @GetMapping("/{userId}/following/posts")
    public ResponseEntity<ApiResponse> getPostsFromFollowing(@PathVariable Long userId) {
        try {
            List<PostDTO> posts = userService.getPostsFromFollowing(userId);
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(200, "Posts found", posts));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
