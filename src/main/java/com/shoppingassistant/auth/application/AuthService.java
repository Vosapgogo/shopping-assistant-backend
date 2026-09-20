package com.shoppingassistant.auth.application;

import com.shoppingassistant.auth.domain.User;
import com.shoppingassistant.auth.infrastructure.JwtService;
import com.shoppingassistant.auth.infrastructure.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public User register(String name, String email, String rawPassword) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists");
        }

        String hashedPassword = passwordEncoder.encode(rawPassword);
        User user = new User(name, email, hashedPassword);
        return userRepository.save(user);
    }

    public String login(String email, String rawPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AuthException(AuthException.Code.EMAIL_NOT_FOUND,
                        "No account found with this email"));

        if (!passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
            throw new AuthException(AuthException.Code.WRONG_PASSWORD, "Incorrect password");
        }

        return jwtService.generateToken(user.getEmail());
    }
}