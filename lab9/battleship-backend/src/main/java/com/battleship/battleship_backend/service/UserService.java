package com.battleship.battleship_backend.service;

import com.battleship.battleship_backend.entity.User;
import com.battleship.battleship_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User authenticate(String username, String password) throws Exception {
        Optional<User> userOpt = userRepository.findByUsername(username);

        if (userOpt.isEmpty()) {
            throw new Exception("Invalid username or password");
        }

        User user = userOpt.get();

        // In a real application, you should use password hashing (BCrypt, etc.)
        if (!user.getPassword().equals(password)) {
            throw new Exception("Invalid username or password");
        }

        user.setLastLogin(LocalDateTime.now());
        return userRepository.save(user);
    }

    public User register(String username, String password) throws Exception {
        if (userRepository.existsByUsername(username)) {
            throw new Exception("Username already exists");
        }

        if (username == null || username.trim().isEmpty()) {
            throw new Exception("Username cannot be empty");
        }

        if (password == null || password.length() < 4) {
            throw new Exception("Password must be at least 4 characters long");
        }

        User user = new User(username.trim(), password);
        return userRepository.save(user);
    }

    public User findById(Long id) throws Exception {
        return userRepository.findById(id)
                .orElseThrow(() -> new Exception("User not found"));
    }
}
