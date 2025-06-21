package com.battleships.battleships_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.battleships.battleships_backend.model.Game;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    @Query("SELECT g FROM Game g WHERE g.gameStatus = 'WAITING'")
    Game findWaitingGame();

    @Query("SELECT g FROM Game g WHERE (g.player1Username = ?1 OR g.player2Username = ?1) AND g.gameStatus = 'ACTIVE'")
    Game findActiveGameByPlayer(String username);
}
