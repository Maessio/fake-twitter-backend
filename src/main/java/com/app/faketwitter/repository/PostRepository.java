package com.app.faketwitter.repository;

import com.app.faketwitter.model.Post;
import com.app.faketwitter.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUser(User user);
    List<Post> findByUserIn(Set<User> users);

    @Query("SELECT p FROM Post p WHERE p.user.id != :userId ORDER BY RANDOM() LIMIT 20")
    List<Post> findRandomPostsExcludingSelf(@Param("userId") Long userId);


}