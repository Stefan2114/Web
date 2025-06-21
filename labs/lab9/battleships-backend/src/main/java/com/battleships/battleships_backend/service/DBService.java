package com.battleships.battleships_backend.service;

import org.springframework.stereotype.Service;

import com.battleships.battleships_backend.model.User;
import com.battleships.battleships_backend.repository.UserRepository;

@Service
public class DBService {

    private final UserRepository userRepository;

    public DBService(UserRepository userRepository) {
        this.userRepository = userRepository;
        // insertUsers();
    }

    private void insertUsers() {
        User user1 = new User("stef1", "stef1");
        User user2 = new User("stef2", "stef2");
        this.userRepository.save(user1);
        this.userRepository.save(user2);
    }
}
