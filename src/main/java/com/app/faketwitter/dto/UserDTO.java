package com.app.faketwitter.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL) // Excludes null fields from serialization
public class UserDTO {

    private Long id;
    private String username;
    private String email;
    private int followersCount;
    private int followingCount;
    private List<PostDTO> posts;

    // Constructor
    public UserDTO() {
    }

    public UserDTO(Long id, String username, String email, int followersCount, int followingCount, List<PostDTO> posts) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.followersCount = followersCount;
        this.followingCount = followingCount;
        this.posts = posts;
    }

    public UserDTO(Long id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return username;
    }

    public void setUserName(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(int followingCount) {
        this.followingCount = followingCount;
    }

    public List<PostDTO> getPosts() {
        return posts;
    }

    public void setPosts(List<PostDTO> postDTO) {
        this.posts = postDTO;
    }
}
