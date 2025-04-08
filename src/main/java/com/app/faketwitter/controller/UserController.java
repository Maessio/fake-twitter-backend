package com.app.faketwitter.controller;

import com.app.faketwitter.dto.UserDTO;
import com.app.faketwitter.response.ApiResponse;
import com.app.faketwitter.service.UserService;
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

    @GetMapping("/{currentUserId}")
    public ResponseEntity<ApiResponse> getUserProfile(
            @PathVariable Long currentUserId,
            @RequestParam(value = "userId", required = false) Long targetUserId) {

        try {
            UserDTO user;

            // If no target user is provided, the logged-in user will be loaded
            if (targetUserId == null) {
                user = userService.getUserProfile(currentUserId);
            } else {

                if (currentUserId.equals(targetUserId)) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(ApiResponse.error(400, "Users cannot search themselves"));
                }

                user = userService.getUserProfile(targetUserId);

                // Set a boolean true if target user is following logged-in user
                boolean isFollowing = userService.isFollowing(currentUserId, targetUserId);
                user.setFollowing(isFollowing);
            }

            return ResponseEntity.status(HttpStatus.OK)
                    .body(ApiResponse.success(200, "Profile found", user));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(404, "User not found"));
        }
    }


    @GetMapping("/search")
    public ResponseEntity<ApiResponse> searchUsers(@RequestParam String username) {

        if (username.isBlank()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(400, "User name cannot to be blank"));
        }

        try {
            List<UserDTO> users = userService.searchUsers(username.toLowerCase().trim());

            if (users.isEmpty()){
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponse.success(200, "Profile not found", null));
            }

            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(200, "Profile found", users));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(404, "User not found"));
        }
    }

    @PostMapping("/{userId}/follow/{followId}")
    public ResponseEntity<ApiResponse> followUser(@PathVariable Long userId, @PathVariable Long followId) {

        if (userId.equals(followId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(400, "User cannot follow themselves"));
        }

        try {
            userService.followUser(userId, followId);

            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(201, "User followed successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(404, "User not found"));
        }
    }

    @DeleteMapping("/{userId}/unfollow/{followId}")
    public ResponseEntity<ApiResponse> unfollowUser(@PathVariable Long userId, @PathVariable Long followId) {

        if (userId.equals(followId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(400, "User cannot unfollow themselves"));
        }

        try {
            userService.unfollowUser(userId, followId);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ApiResponse.success(200, "User unfollowed successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(404, "User not found"));
        }
    }


}
