package com.battleship.battleship_backend.repository;

import com.battleship.battleship_backend.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

    @Query("SELECT g FROM Game g WHERE g.status = 'WAITING_FOR_PLAYERS' OR g.status = 'WAITING_FOR_SHIP_PLACEMENT'")
    List<Game> findActiveGames();

    @Query("SELECT g FROM Game g WHERE (g.player1Id = ?1 OR g.player2Id = ?1) AND g.status != 'FINISHED'")
    Optional<Game> findActiveGameByPlayerId(Long playerId);

    @Query("SELECT g FROM Game g WHERE g.status = 'WAITING_FOR_PLAYERS'")
    Optional<Game> findWaitingGame();
}