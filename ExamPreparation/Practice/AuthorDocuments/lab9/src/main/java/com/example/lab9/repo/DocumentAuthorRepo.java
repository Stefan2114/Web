package com.example.lab9.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.lab9.model.DocumentAuthor;

@Repository
public interface DocumentAuthorRepo extends JpaRepository<DocumentAuthor, Integer> {

    @Query("SELECT da FROM DocumentAuthor da WHERE da.author.id = :authorId")
    List<DocumentAuthor> findAllByAuthorId(@Param("authorId") Integer authorId);

}
