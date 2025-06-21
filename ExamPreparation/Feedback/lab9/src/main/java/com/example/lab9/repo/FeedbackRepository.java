package com.example.lab9.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.lab9.model.Feedback;

public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {
}
