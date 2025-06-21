package com.example.lab9.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.lab9.model.Author;

@Repository
public interface AuthorRepo extends JpaRepository<Author, Integer> {

    Optional<Author> findByName(String authorName);
}
