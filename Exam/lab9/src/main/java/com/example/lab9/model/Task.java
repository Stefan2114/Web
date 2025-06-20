package com.example.lab9.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import com.example.lab9.Enum.StatusType;

@Entity
@Data
@Table(name = "Tasks")

@NoArgsConstructor
@AllArgsConstructor
public class Task {
    @Id
    // makes it auto increment
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(length = 64, nullable = false)
    private StatusType status;

    @ManyToOne
    @JoinColumn(name = "UserId", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDateTime lastUpdated;

}