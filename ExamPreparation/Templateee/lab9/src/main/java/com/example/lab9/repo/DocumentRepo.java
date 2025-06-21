package com.example.lab9.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.lab9.model.Document;

@Repository
public interface DocumentRepo extends JpaRepository<Document, Integer> {

}
