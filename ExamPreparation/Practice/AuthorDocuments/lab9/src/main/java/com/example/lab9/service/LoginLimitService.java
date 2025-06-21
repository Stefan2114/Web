package com.example.lab9.service;

import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;

@Service
public class LoginLimitService {

    private final SessionRegistry sessionRegistry;
    private final int maxConcurrentUsers = 10;

    public LoginLimitService(SessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }

    public int getCurrentActiveUsers() {
        return sessionRegistry.getAllPrincipals().size();
    }

    public boolean canUserLogin(String username) {

        boolean userAlreadyLoggedIn = sessionRegistry.getAllPrincipals().stream()
                .anyMatch(principal -> principal.toString().equals(username));

        if (userAlreadyLoggedIn) {
            return true;
        }
        int currentUsers = getCurrentActiveUsers();

        return currentUsers < maxConcurrentUsers;
    }
}
