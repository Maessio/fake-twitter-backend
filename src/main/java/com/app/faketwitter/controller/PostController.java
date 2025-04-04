package com.app.faketwitter.controller;

import com.app.faketwitter.dto.PostDTO;
import com.app.faketwitter.request.CreatePostRequest;
import com.app.faketwitter.response.ApiResponse;
import com.app.faketwitter.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private UserService userService;

    @PostMapping("/{userId}/posts")
    public ResponseEntity<ApiResponse> createPost(@PathVariable Long userId, @Valid @RequestBody CreatePostRequest content) {

        try {
            userService.createPost(userId, content.getContent());

            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(201, "Post created successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(404, "User not found"));
        }
    }

    @GetMapping("/{userId}/following/posts")
    public ResponseEntity<ApiResponse> getPostsFromFollowing(@PathVariable Long userId) {

        try {
            List<PostDTO> posts = userService.getPostsFromFollowing(userId);

            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(200, "Posts found", posts));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(404, "User not found"));
        }
    }

    @GetMapping("/{userId}/random/posts")
    public ResponseEntity<ApiResponse> getRandomPosts(@PathVariable Long userId) {
        try {
            List<PostDTO> posts = userService.getRandomPosts(userId);
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(200, "Found posts from users you are not following", posts));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(404, "User not found"));
        }
    }

}
