package com.shoppingassistant.auth.infrastructure;

import com.shoppingassistant.auth.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Find a user by email to check uniqueness or handle login
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}