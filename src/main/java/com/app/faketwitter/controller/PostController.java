package com.app.faketwitter.controller;

import com.app.faketwitter.dto.PostDTO;
import com.app.faketwitter.request.CreatePostRequest;
import com.app.faketwitter.response.ApiResponse;
import com.app.faketwitter.service.PostService;
import com.app.faketwitter.service.UserService;
import jakarta.persistence.EntityNotFoundException;
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
    private PostService postService;

    @PostMapping("/{userId}")
    public ResponseEntity<ApiResponse> createPost(@PathVariable Long userId, @Valid @RequestBody CreatePostRequest content) {

        try {
            postService.createPost(userId, content.getContent());

            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(201, "Post created successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error(401, "User not found"));
        }
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<ApiResponse> deletePost(@PathVariable Long postId) {
        try {
            postService.deletePost(postId);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(ApiResponse.success(200, "Post deleted successfully", null));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(404, "Post not found"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, "An error occurred while deleting the post"));
        }
    }
    
    @GetMapping("/{userId}/following")
    public ResponseEntity<ApiResponse> getPostsFromFollowing(@PathVariable Long userId) {

        try {

            List<PostDTO> posts = postService.getPostsFromFollowing(userId);

            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(200, "Posts found", posts));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(404, "User not found"));
        }
    }

    @GetMapping("/random")
    public ResponseEntity<ApiResponse> getRandomPosts() {
        try {
            List<PostDTO> posts = postService.getRandomPosts();
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(200, "Found posts from users you are not following", posts));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(404, "User not found"));
        }
    }

}
