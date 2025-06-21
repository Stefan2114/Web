package com.example.lab9.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.lab9.model.Author;
import com.example.lab9.repo.AuthorRepo;

@Service
public class AuthorService {

    private final AuthorRepo authorRepo;

    public AuthorService(AuthorRepo authorRepo) {
        this.authorRepo = authorRepo;
    }

    public List<Author> getAll() {
        return this.authorRepo.findAll();
    }
}
