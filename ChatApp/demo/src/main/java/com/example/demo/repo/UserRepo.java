package com.example.demo.repo;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public class UserRepo {

    private final Map<String, String> users = new HashMap<>();

    public UserRepo() {

        users.put("stef", "stef");
        users.put("andrei", "123");
    }

    public boolean authenticate(String username, String password) {
        return users.containsKey(username) && users.get(username).equals(password);
    }
}
