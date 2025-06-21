package com.example.lab9.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.lab9.model.TaskLog;

public interface TaskLogRepository extends JpaRepository<TaskLog, Integer> {
}