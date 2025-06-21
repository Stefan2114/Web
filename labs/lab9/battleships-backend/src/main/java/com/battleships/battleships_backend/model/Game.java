package com.battleships.battleships_backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "games")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String player1Username;
    private String player2Username;
    private String player1Grid;
    private String player2Grid;
    private String player1Attacks;
    private String player2Attacks;
    private String currentPlayer;
    private String gameStatus; // WAITING, ACTIVE, FINISHED
    private String winner;

    public Game() {
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlayer1Username() {
        return player1Username;
    }

    public void setPlayer1Username(String player1Username) {
        this.player1Username = player1Username;
    }

    public String getPlayer2Username() {
        return player2Username;
    }

    public void setPlayer2Username(String player2Username) {
        this.player2Username = player2Username;
    }

    public String getPlayer1Grid() {
        return player1Grid;
    }

    public void setPlayer1Grid(String player1Grid) {
        this.player1Grid = player1Grid;
    }

    public String getPlayer2Grid() {
        return player2Grid;
    }

    public void setPlayer2Grid(String player2Grid) {
        this.player2Grid = player2Grid;
    }

    public String getPlayer1Attacks() {
        return player1Attacks;
    }

    public void setPlayer1Attacks(String player1Attacks) {
        this.player1Attacks = player1Attacks;
    }

    public String getPlayer2Attacks() {
        return player2Attacks;
    }

    public void setPlayer2Attacks(String player2Attacks) {
        this.player2Attacks = player2Attacks;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public String getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(String gameStatus) {
        this.gameStatus = gameStatus;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }
}
