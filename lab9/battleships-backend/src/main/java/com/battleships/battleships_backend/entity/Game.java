package com.battleships.battleships_backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "games")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "player1_id")
    private Long player1Id;

    @Column(name = "player2_id")
    private Long player2Id;

    @Column(name = "current_player_id")
    private Long currentPlayerId;

    @Enumerated(EnumType.STRING)
    private GameStatus status;

    @Column(name = "winner_id")
    private Long winnerId;

    @Column(name = "player1_ships")
    private String player1Ships; // JSON string representing ship positions

    @Column(name = "player2_ships")
    private String player2Ships; // JSON string representing ship positions

    @Column(name = "player1_moves")
    private String player1Moves; // JSON string representing moves

    @Column(name = "player2_moves")
    private String player2Moves; // JSON string representing moves

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum GameStatus {
        WAITING_FOR_PLAYERS,
        WAITING_FOR_SHIP_PLACEMENT,
        IN_PROGRESS,
        FINISHED
    }

    // Constructors
    public Game() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.status = GameStatus.WAITING_FOR_PLAYERS;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPlayer1Id() {
        return player1Id;
    }

    public void setPlayer1Id(Long player1Id) {
        this.player1Id = player1Id;
    }

    public Long getPlayer2Id() {
        return player2Id;
    }

    public void setPlayer2Id(Long player2Id) {
        this.player2Id = player2Id;
    }

    public Long getCurrentPlayerId() {
        return currentPlayerId;
    }

    public void setCurrentPlayerId(Long currentPlayerId) {
        this.currentPlayerId = currentPlayerId;
    }

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }

    public Long getWinnerId() {
        return winnerId;
    }

    public void setWinnerId(Long winnerId) {
        this.winnerId = winnerId;
    }

    public String getPlayer1Ships() {
        return player1Ships;
    }

    public void setPlayer1Ships(String player1Ships) {
        this.player1Ships = player1Ships;
    }

    public String getPlayer2Ships() {
        return player2Ships;
    }

    public void setPlayer2Ships(String player2Ships) {
        this.player2Ships = player2Ships;
    }

    public String getPlayer1Moves() {
        return player1Moves;
    }

    public void setPlayer1Moves(String player1Moves) {
        this.player1Moves = player1Moves;
    }

    public String getPlayer2Moves() {
        return player2Moves;
    }

    public void setPlayer2Moves(String player2Moves) {
        this.player2Moves = player2Moves;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}