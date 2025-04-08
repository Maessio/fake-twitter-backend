package com.app.faketwitter.unit.controllers;

import com.app.faketwitter.controller.PostController;
import com.app.faketwitter.dto.PostDTO;
import com.app.faketwitter.request.CreatePostRequest;
import com.app.faketwitter.response.ApiResponse;
import com.app.faketwitter.service.PostService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostControllerTest {

    @InjectMocks
    private PostController postController;

    @Mock
    private PostService postService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreatePost_Success() throws Exception {
        Long userId = 1L;
        String content = "Test post content";
        CreatePostRequest request = new CreatePostRequest(content);

        // Simulando o comportamento esperado do serviço
        doNothing().when(postService).createPost(userId, content);

        // Chama o método do controller
        ResponseEntity<ApiResponse> response = postController.createPost(userId, request);

        // Verifica o status da resposta
        assertEquals(201, response.getStatusCode().value());
        assertTrue(((ApiResponse) response.getBody()).getMessage().contains("Post created successfully"));
    }

    @Test
    void testCreatePost_UserNotFound() throws Exception {
        Long userId = 1L;
        String content = "Test post content";
        CreatePostRequest request = new CreatePostRequest(content);

        // Simulando uma exceção quando o serviço tentar criar o post
        doThrow(new Exception("User not found")).when(postService).createPost(userId, content);

        // Chama o método do controller
        ResponseEntity<ApiResponse> response = postController.createPost(userId, request);

        // Verifica o status da resposta e a mensagem de erro
        assertEquals(401, response.getStatusCode().value());
        assertTrue(((ApiResponse) response.getBody()).getMessage().contains("User not found"));
    }

    @Test
    void testDeletePost_Success() {
        Long postId = 1L;

        // Simula o comportamento do serviço para deletar o post
        doNothing().when(postService).deletePost(postId);

        ResponseEntity<ApiResponse> response = postController.deletePost(postId);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Post deleted successfully", response.getBody().getMessage());
    }

    @Test
    void testDeletePost_PostNotFound() {
        Long postId = 1L;

        // Simula a exceção de post não encontrado
        doThrow(new EntityNotFoundException("Post not found")).when(postService).deletePost(postId);

        ResponseEntity<ApiResponse> response = postController.deletePost(postId);

        assertEquals(404, response.getStatusCode().value());
        assertEquals("Post not found", response.getBody().getMessage());
    }

    @Test
    void testGetPostsFromFollowing_Success() throws Exception {
        Long userId = 1L;
        PostDTO postDTO = new PostDTO("Test content", userId, "user1");
        when(postService.getPostsFromFollowing(userId)).thenReturn(List.of(postDTO));

        ResponseEntity<ApiResponse> response = postController.getPostsFromFollowing(userId);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Posts found", response.getBody().getMessage());
    }

    @Test
    void testGetPostsFromFollowing_UserNotFound() throws Exception {
        Long userId = 1L;
        when(postService.getPostsFromFollowing(userId)).thenThrow(new Exception("User not found"));

        ResponseEntity<ApiResponse> response = postController.getPostsFromFollowing(userId);

        assertEquals(404, response.getStatusCode().value());
        assertEquals("User not found", response.getBody().getMessage());
    }

    @Test
    void testGetRandomPosts_Success() {
        PostDTO postDTO = new PostDTO("Random post content", 1L, "user1");
        when(postService.getRandomPosts()).thenReturn(List.of(postDTO));

        ResponseEntity<ApiResponse> response = postController.getRandomPosts();

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Found posts from users you are not following", response.getBody().getMessage());
    }

    @Test
    void testGetRandomPosts_Failure() {
        when(postService.getRandomPosts()).thenThrow(new RuntimeException("Error fetching random posts"));

        ResponseEntity<ApiResponse> response = postController.getRandomPosts();

        assertEquals(404, response.getStatusCode().value());
        assertEquals("User not found", response.getBody().getMessage());
    }
}
