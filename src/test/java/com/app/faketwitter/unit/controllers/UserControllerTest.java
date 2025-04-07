package com.app.faketwitter.unit.controllers;

import com.app.faketwitter.controller.UserController;
import com.app.faketwitter.dto.PostDTO;
import com.app.faketwitter.dto.UserDTO;
import com.app.faketwitter.request.CreatePostRequest;
import com.app.faketwitter.response.ApiResponse;
import com.app.faketwitter.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void testGetUserProfile_Success() throws Exception {
        UserDTO userDTO = new UserDTO(1L, "testUser", "test@example.com", 10, 5, Collections.emptyList());
        when(userService.getUserProfile(1L)).thenReturn(userDTO);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Profile found"));
    }

    @Test
    void testGetUserProfile_UserNotFound() throws Exception {
        when(userService.getUserProfile(anyLong())).thenThrow(new Exception("User not found"));

        mockMvc.perform(get("/users/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User not found"));
    }

    @Test
    void testFollowUser_Success() throws Exception {
        mockMvc.perform(post("/users/1/follow/2"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("User followed successfully"));
    }

    @Test
    void testFollowUser_SelfFollow() throws Exception {
        mockMvc.perform(post("/users/1/follow/1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("User cannot follow themselves"));
    }

    @Test
    void testUnfollowUser_Success() throws Exception {
        mockMvc.perform(delete("/users/1/unfollow/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User unfollowed successfully"));
    }

    @Test
    void testUnfollowUser_SelfUnfollow() throws Exception {
        mockMvc.perform(delete("/users/1/unfollow/1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("User cannot unfollow themselves"));
    }

    @Test
    void testSearchUsers_Success() throws Exception {
        UserDTO userDTO = new UserDTO(1L, "testUser", "test@example.com", 10, 5, Collections.emptyList());
        when(userService.searchUsers("test")).thenReturn(List.of(userDTO));

        mockMvc.perform(get("/users/search").param("username", "test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Profile found"));
    }

    @Test
    void testSearchUsers_BlankUsername() throws Exception {
        mockMvc.perform(get("/users/search").param("username", " "))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("User name cannot to be blank"));
    }

    @Test
    void testSearchUsers_NotFound() throws Exception {
        when(userService.searchUsers("xyz")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/users/search").param("username", "xyz"))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.message").value("Profile not found"));
    }

}
