package com.app.faketwitter.service;

import com.app.faketwitter.dto.PostDTO;
import com.app.faketwitter.dto.UserDTO;
import com.app.faketwitter.model.Post;
import com.app.faketwitter.model.User;
import com.app.faketwitter.repository.PostRepository;
import com.app.faketwitter.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public UserDTO getUserProfile(Long userId) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("User not found"));

        int followersCount = getFollowersCount(userId);
        int followingCount = getFollowingCount(userId);

        List<Post> posts = postRepository.findByUser(user);

        List<PostDTO> postDTOs = posts.stream()
                .map(post -> new PostDTO(post.getContent(), post.getUser().getUsername()))
                .collect(Collectors.toList());

        return new UserDTO(user.getId(), user.getUsername(), user.getEmail(), followersCount, followingCount, postDTOs);
    }

    @Transactional
    public User followUser(Long userId, Long followId) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("User not found"));
        User toFollow = userRepository.findById(followId)
                .orElseThrow(() -> new Exception("User to follow not found"));

        user.getFollowing().add(toFollow);
        toFollow.getFollowers().add(user);

        userRepository.save(user);
        userRepository.save(toFollow);

        return user;
    }

    @Transactional
    public void createPost(Long userId, String content) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("User not found"));

        Post post = new Post();
        post.setContent(content);
        post.setUser(user);

        postRepository.save(post);
    }

    @Transactional
    public List<PostDTO> getPostsFromFollowing(Long userId) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("User not found"));

        List<Post> posts = postRepository.findByUserIn(user.getFollowing());

        return posts.stream().map(post -> new PostDTO(post.getContent(), post.getUser().getId(), post.getUser().getUsername())).toList();
    }

    public List<UserDTO> searchUsers(String query) {
        List<User> users = userRepository.findByUsernameContainingIgnoreCase(query);

        return users.stream()
                .map(user -> new UserDTO(user.getId(), user.getUsername(), user.getEmail()))
                .collect(Collectors.toList());
    }

    private int getFollowersCount(Long userId) {
        String qlString = "SELECT COUNT(u) FROM User u JOIN u.following f WHERE f.id = :userId";
        TypedQuery<Long> query = entityManager.createQuery(qlString, Long.class);
        query.setParameter("userId", userId);
        return query.getSingleResult().intValue();
    }

    private int getFollowingCount(Long userId) {
        String qlString = "SELECT COUNT(f) FROM User u JOIN u.following f WHERE u.id = :userId";
        TypedQuery<Long> query = entityManager.createQuery(qlString, Long.class);
        query.setParameter("userId", userId);
        return query.getSingleResult().intValue();
    }

}
