package com.app.faketwitter.service;

import com.app.faketwitter.dto.PostDTO;
import com.app.faketwitter.model.Post;
import com.app.faketwitter.model.User;
import com.app.faketwitter.repository.PostRepository;
import com.app.faketwitter.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void createPost(Long userId, String content) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("User not found"));

        Post post = new Post();
        post.setContent(content);
        post.setUser(user);

        postRepository.save(post);
    }

    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));

        postRepository.delete(post);
    }

    @Transactional
    public List<PostDTO> getPostsFromFollowing(Long userId) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("User not found"));

        List<Post> posts = postRepository.findByUserIn(user.getFollowing());

        return posts.stream().map(post -> new PostDTO(post.getContent(), post.getUser().getId(), post.getUser().getUsername())).toList();
    }


    public List<PostDTO> getRandomPosts() {

        List<Post> randomPosts = postRepository.findRandomPosts();

        return randomPosts.stream()
                .map(post -> new PostDTO(
                        post.getId(),
                        post.getContent(),
                        post.getUser().getId(),
                        post.getUser().getUsername()
                ))
                .collect(Collectors.toList());
    }
}
