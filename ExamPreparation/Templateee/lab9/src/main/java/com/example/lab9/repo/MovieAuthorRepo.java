package com.example.lab9.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.lab9.model.MovieAuthor;

@Repository
public interface MovieAuthorRepo extends JpaRepository<MovieAuthor, Integer> {

    @Query("SELECT ma FROM MovieAuthor ma WHERE ma.author.id = :authorId")
    List<MovieAuthor> findAllByAuthorId(@Param("authorId") Integer authorId);
}