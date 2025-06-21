package com.example.lab9.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.lab9.model.Author;
import com.example.lab9.model.Document;
import com.example.lab9.model.Movie;
import com.example.lab9.service.AuthorService;
import com.example.lab9.service.DocumentService;
import com.example.lab9.service.MovieService;

@RestController
@RequestMapping("api/authors")
public class AuthorController {

    private final AuthorService authorService;
    private final DocumentService documentService;
    private final MovieService movieService;

    public AuthorController(AuthorService authorService, DocumentService documentService, MovieService movieService) {
        this.authorService = authorService;
        this.documentService = documentService;
        this.movieService = movieService;
    }

    @GetMapping
    public ResponseEntity<List<Author>> getAllAuthors() {
        System.out.println("Trying to get authors");
        List<Author> authors = this.authorService.getAll();
        for (Author author : authors) {
            System.out.println(author.getName());
        }
        return ResponseEntity.ok(authors);
    }

    @GetMapping("/{id}/documents")
    public ResponseEntity<List<Document>> getAuthorDocuments(@PathVariable Integer id) {
        return ResponseEntity.ok(this.documentService.getAuthorDocuments(id));
    }

    @GetMapping("/{id}/movies")
    public ResponseEntity<List<Movie>> getAuthorMovies(@PathVariable Integer id) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // this is usually the username
        System.out.println(username);
        return ResponseEntity.ok(this.movieService.getAuthorMovies(id));
    }
}
