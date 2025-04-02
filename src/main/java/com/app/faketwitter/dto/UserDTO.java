package com.app.faketwitter.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL) // Excludes null fields from serialization
public class UserDTO {

    private Long id;
    private String userName;
    private String email;
    private List<PostDTO> posts;

    // Constructor
    public UserDTO(Long id, String userName, String email, List<PostDTO> postDTO) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.posts = postDTO;
    }

    public UserDTO(Long id, String userName, String email) {
        this.id = id;
        this.userName = userName;
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
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<PostDTO> getPostDTO() {
        return posts;
    }

    public void setPostDTO(List<PostDTO> postDTO) {
        this.posts = postDTO;
    }
}
