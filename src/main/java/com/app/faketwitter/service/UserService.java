package com.app.faketwitter.service;

import com.app.faketwitter.dto.PostDTO;
import com.app.faketwitter.dto.UserDTO;
import com.app.faketwitter.model.Post;
import com.app.faketwitter.model.User;
import com.app.faketwitter.repository.PostRepository;
import com.app.faketwitter.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@NoArgsConstructor
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

        List<PostDTO> postDTO = posts.stream()
                .sorted(Comparator.comparing(Post::getId).reversed())
                .map(post -> new PostDTO(post.getId(), post.getContent(), post.getUser().getUsername()))
                .collect(Collectors.toList());

        return new UserDTO(user.getId(), user.getUsername(), user.getEmail(), followersCount, followingCount, postDTO);
    }

    @Transactional
    public User followUser(Long userId, Long followId) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("User not found"));
        User toFollow = userRepository.findById(followId)
                .orElseThrow(() -> new Exception("User to follow not found"));

        if (user.getFollowing().contains(toFollow)) {
            throw new Exception("User is already following the target user");
        }

        user.getFollowing().add(toFollow);
        toFollow.getFollowers().add(user);

        userRepository.save(user);
        userRepository.save(toFollow);

        return user;
    }

    @Transactional
    public User unfollowUser(Long userId, Long followId) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("User not found"));
        User toUnfollow = userRepository.findById(followId)
                .orElseThrow(() -> new Exception("User to unfollow not found"));

        user.getFollowing().remove(toUnfollow);
        toUnfollow.getFollowers().remove(user);

        userRepository.save(user);
        userRepository.save(toUnfollow);

        return user;
    }

    public List<UserDTO> searchUsers(String query) {
        List<User> users = userRepository.findTop5ByUsernameContainingIgnoreCase(query);

        return users.stream()
                .map(user -> new UserDTO(user.getId(), user.getUsername()))
                .collect(Collectors.toList());
    }

    public Long getUserId(String email) {
        UserDetails userDetails = userRepository.findByEmail(email);

        if (userDetails instanceof User user) {
            return user.getId();
        }

        throw new EntityNotFoundException("User not found");
    }

    public boolean isFollowing(Long currentUserId, Long targetUserId) throws Exception {
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new Exception("Current user not found"));

        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new Exception("Target user not found"));

        return currentUser.getFollowing().contains(targetUser);
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
