package com.app.faketwitter.unit.services;

import com.app.faketwitter.dto.PostDTO;
import com.app.faketwitter.model.Post;
import com.app.faketwitter.model.User;
import com.app.faketwitter.repository.PostRepository;
import com.app.faketwitter.repository.UserRepository;
import com.app.faketwitter.service.PostService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PostServiceTest {

    @InjectMocks
    private PostService postService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);
        user.setUsername("testUser");
    }

    @Test
    void testCreatePost_Success() throws Exception {
        String content = "Test post content";
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        postService.createPost(1L, content);

        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    void testCreatePost_UserNotFound() {
        String content = "Test post content";
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> postService.createPost(1L, content));

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void testDeletePost_Success() {
        Long postId = 1L;
        Post post = new Post();
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        postService.deletePost(postId);

        verify(postRepository, times(1)).delete(post);
    }

    @Test
    void testDeletePost_PostNotFound() {
        Long postId = 1L;
        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> postService.deletePost(postId));

        assertEquals("Post not found", exception.getMessage());
    }

    @Test
    void testGetPostsFromFollowing_Success() throws Exception {
        Long userId = 1L;
        User followedUser = new User();
        followedUser.setId(2L);
        followedUser.setUsername("followedUser");

        Post post1 = new Post();
        post1.setContent("Post 1 content");
        post1.setUser(followedUser);

        Post post2 = new Post();
        post2.setContent("Post 2 content");
        post2.setUser(followedUser);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(postRepository.findByUserIn(user.getFollowing())).thenReturn(Arrays.asList(post1, post2));

        List<PostDTO> posts = postService.getPostsFromFollowing(userId);

        assertEquals(2, posts.size());
        assertEquals("Post 1 content", posts.get(0).getContent());
    }

    @Test
    void testGetPostsFromFollowing_UserNotFound() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> postService.getPostsFromFollowing(userId));

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void testGetRandomPosts_Success() {
        Post post1 = new Post();
        post1.setContent("Random post 1");
        post1.setUser(user);

        Post post2 = new Post();
        post2.setContent("Random post 2");
        post2.setUser(user);

        when(postRepository.findRandomPosts()).thenReturn(Arrays.asList(post1, post2));

        List<PostDTO> posts = postService.getRandomPosts();

        assertEquals(2, posts.size());
        assertEquals("Random post 1", posts.get(0).getContent());
    }
}
