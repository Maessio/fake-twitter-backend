package com.app.faketwitter.repository;

import com.app.faketwitter.model.Post;
import com.app.faketwitter.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUser(User user);
    List<Post> findByUserIn(Set<User> users);

    @Query(value = "SELECT * FROM posts ORDER BY RANDOM() LIMIT 20", nativeQuery = true)
    List<Post> findRandomPosts();

}