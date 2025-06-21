package com.example.lab9.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.lab9.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {

    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);
}