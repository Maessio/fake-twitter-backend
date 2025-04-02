package com.app.faketwitter.repository;

import com.app.faketwitter.model.Post;
import com.app.faketwitter.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);

    @Query("SELECT p FROM Post p WHERE p.user IN :following")
    List<Post> findPostsFromFollowing(@Param("following") List<User> following);

    List<User> findByUsernameContainingIgnoreCase(String query);
}

