package com.app.faketwitter.repository;

import com.app.faketwitter.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    UserDetails findByEmail(String email);
    Optional<User> findByUsername(String username);

    List<User> findByUsernameContainingIgnoreCase(String query);
}

