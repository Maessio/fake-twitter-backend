package com.app.faketwitter.repository;

import com.app.faketwitter.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    UserDetails findByEmail(String email);
    Optional<User> findByUsername(String username);

    List<User> findTop5ByUsernameContainingIgnoreCase(String query);

    @Query("SELECT u.id FROM User u JOIN u.following f WHERE f.id = :userId")
    List<Long> getFollowingIds(@Param("userId") Long userId);
}

