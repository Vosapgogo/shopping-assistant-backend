package com.shoppingassistant.auth.application;

import com.shoppingassistant.auth.domain.User;
import com.shoppingassistant.auth.infrastructure.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public User register(String name, String email, String rawPassword) {
        // Check if email already exists
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists");
        }

        // Hash the password using BCrypt
        String hashedPassword = passwordEncoder.encode(rawPassword);

        // Create and save the new user with name
        User user = new User(name, email, hashedPassword);
        return userRepository.save(user);
    }
}