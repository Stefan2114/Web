package com.example.lab9.service;

import org.springframework.stereotype.Service;

import com.example.lab9.model.Author;
import com.example.lab9.repo.AuthorRepo;
import com.example.lab9.repo.DocumentAuthorRepo;
import com.example.lab9.repo.DocumentRepo;

@Service
public class DBService {
    private final DocumentRepo documentRepo;
    private final AuthorRepo authorRepo;
    private final DocumentAuthorRepo documentAuthorRepo;

    public DBService(DocumentRepo documentRepo, AuthorRepo authorRepo, DocumentAuthorRepo documentAuthorRepo) {
        this.documentRepo = documentRepo;
        this.authorRepo = authorRepo;
        this.documentAuthorRepo = documentAuthorRepo;

        // makeInserts();

    }

    private void makeInserts() {
        Author author1 = new Author();
        author1.setName("stef");
        this.authorRepo.save(author1);

        Author author2 = new Author();
        author2.setName("marian");
        this.authorRepo.save(author2);

        Author author3 = new Author();
        author3.setName("andrei");
        this.authorRepo.save(author3);
    }

}
