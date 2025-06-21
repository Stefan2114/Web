package com.example.lab9.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.lab9.model.Movie;
import com.example.lab9.repo.MovieAuthorRepo;
import com.example.lab9.repo.MovieRepo;

@Service
@Transactional
public class MovieService {

    private final MovieRepo movieRepo;
    private final MovieAuthorRepo movieAuthorRepo;

    public MovieService(MovieRepo movieRepo, MovieAuthorRepo movieAuthorRepo) {
        this.movieRepo = movieRepo;
        this.movieAuthorRepo = movieAuthorRepo;

    }

    public List<Movie> getAuthorMovies(Integer authorId) {
        return this.movieAuthorRepo.findAllByAuthorId(authorId).stream()
                .map(movieAuthor -> movieAuthor.getMovie()).toList();
    }

    public void deleteMovie(Integer movieId) {

        this.movieRepo.deleteById(movieId);
    }

}
