package com.app.faketwitter.services;

import com.app.faketwitter.dto.PostDTO;
import com.app.faketwitter.dto.UserDTO;
import com.app.faketwitter.model.Post;
import com.app.faketwitter.model.User;
import com.app.faketwitter.repository.PostRepository;
import com.app.faketwitter.repository.UserRepository;
import com.app.faketwitter.service.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<Long> typedQuery;

    @InjectMocks
    private UserService userService;

    private User user;
    private User followedUser;
    private Post post;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setFollowing(Set.of());
        user.setFollowers(Set.of());

        followedUser = new User();
        followedUser.setId(2L);
        followedUser.setUsername("followedUser");

        post = new Post();
        post.setContent("Hello World");
        post.setUser(user);
    }

    @Test
    void getUserProfile_ShouldReturnUserDTO() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter(anyString(), any())).thenReturn(typedQuery);
        when(typedQuery.getSingleResult()).thenReturn(5L); // Simulando 5 seguidores

        UserDTO userDTO = userService.getUserProfile(1L);

        assertNotNull(userDTO);
        assertEquals("testuser", userDTO.getUserName());
        assertEquals("test@example.com", userDTO.getEmail());
        assertEquals(5, userDTO.getFollowersCount());
    }

    @Test
    void followUser_ShouldAddUserToFollowing() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("user1");
        user.setFollowing(new HashSet<>());

        User toFollow = new User();
        toFollow.setId(2L);
        toFollow.setUsername("user2");
        toFollow.setFollowers(new HashSet<>());

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findById(2L)).thenReturn(Optional.of(toFollow));

        userService.followUser(1L, 2L);

        assertTrue(user.getFollowing().contains(toFollow));
        assertTrue(toFollow.getFollowers().contains(user));

        verify(userRepository, times(2)).save(any(User.class));
    }

    @Test
    void createPost_ShouldSavePost() throws Exception {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.createPost(1L, "New Post");

        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    void getPostsFromFollowing_ShouldReturnPostDTOs() throws Exception {
        user.setFollowing(Set.of(followedUser));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(postRepository.findByUserIn(user.getFollowing())).thenReturn(List.of(post));

        List<PostDTO> posts = userService.getPostsFromFollowing(1L);

        assertNotNull(posts);
        assertFalse(posts.isEmpty());
        assertEquals("Hello World", posts.get(0).getContent());
    }

    @Test
    void searchUsers_ShouldReturnUserDTOList() {
        when(userRepository.findByUsernameContainingIgnoreCase("test"))
                .thenReturn(List.of(user));

        List<UserDTO> users = userService.searchUsers("test");

        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals("testuser", users.get(0).getUserName());
    }
}
