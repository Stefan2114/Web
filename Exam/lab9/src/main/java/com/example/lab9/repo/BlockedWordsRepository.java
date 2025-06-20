package com.example.lab9.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.lab9.model.BlockedWords;

public interface BlockedWordsRepository extends JpaRepository<BlockedWords, Integer> {
}