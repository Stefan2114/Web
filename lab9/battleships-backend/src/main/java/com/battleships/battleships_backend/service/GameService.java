package com.battleships.battleships_backend.service;

import com.battleships.battleships_backend.entity.Game;
import com.battleships.battleships_backend.repository.GameRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final int GRID_SIZE = 8;
    private static final int SHIPS_PER_PLAYER = 2;

    public Game joinOrCreateGame(Long playerId) throws Exception {
        // Check if player is already in an active game
        Optional<Game> activeGame = gameRepository.findActiveGameByPlayerId(playerId);
        if (activeGame.isPresent()) {
            return activeGame.get();
        }

        // Look for a waiting game
        Optional<Game> waitingGame = gameRepository.findWaitingGame();
        if (waitingGame.isPresent()) {
            Game game = waitingGame.get();
            if (game.getPlayer1Id().equals(playerId)) {
                return game; // Same player rejoining
            }

            // Add second player
            game.setPlayer2Id(playerId);
            game.setStatus(Game.GameStatus.WAITING_FOR_SHIP_PLACEMENT);
            game.setCurrentPlayerId(game.getPlayer1Id()); // Player 1 starts
            return gameRepository.save(game);
        }

        // Create new game
        Game newGame = new Game();
        newGame.setPlayer1Id(playerId);
        newGame.setStatus(Game.GameStatus.WAITING_FOR_PLAYERS);
        return gameRepository.save(newGame);
    }

    public Game placeShips(Long gameId, Long playerId, List<Ship> ships) throws Exception {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new Exception("Game not found"));

        if (!playerId.equals(game.getPlayer1Id()) && !playerId.equals(game.getPlayer2Id())) {
            throw new Exception("Player not in this game");
        }

        if (ships.size() != SHIPS_PER_PLAYER) {
            throw new Exception("Must place exactly " + SHIPS_PER_PLAYER + " ships");
        }

        // Validate ship placements
        validateShipPlacements(ships);

        String shipsJson = objectMapper.writeValueAsString(ships);

        if (playerId.equals(game.getPlayer1Id())) {
            game.setPlayer1Ships(shipsJson);
        } else {
            game.setPlayer2Ships(shipsJson);
        }

        // Check if both players have placed ships
        if (game.getPlayer1Ships() != null && game.getPlayer2Ships() != null) {
            game.setStatus(Game.GameStatus.IN_PROGRESS);
        }

        return gameRepository.save(game);
    }

    public MoveResult makeMove(Long gameId, Long playerId, int row, int col) throws Exception {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new Exception("Game not found"));

        if (game.getStatus() != Game.GameStatus.IN_PROGRESS) {
            throw new Exception("Game is not in progress");
        }

        if (!playerId.equals(game.getCurrentPlayerId())) {
            throw new Exception("Not your turn");
        }

        // Validate move coordinates
        if (row < 0 || row >= GRID_SIZE || col < 0 || col >= GRID_SIZE) {
            throw new Exception("Invalid coordinates");
        }

        // Get player's moves
        List<Move> playerMoves = getPlayerMoves(game, playerId);

        // Check if position already attacked
        if (playerMoves.stream().anyMatch(m -> m.getRow() == row && m.getCol() == col)) {
            throw new Exception("Position already attacked");
        }

        // Get opponent's ships
        Long opponentId = playerId.equals(game.getPlayer1Id()) ? game.getPlayer2Id() : game.getPlayer1Id();
        List<Ship> opponentShips = getPlayerShips(game, opponentId);

        // Check if hit
        boolean isHit = opponentShips.stream()
                .anyMatch(ship -> ship.getRow() == row && ship.getCol() == col);

        // Create and save move
        Move move = new Move(row, col, isHit);
        playerMoves.add(move);
        savePlayerMoves(game, playerId, playerMoves);

        // Check for win condition
        boolean gameWon = checkWinCondition(playerMoves, opponentShips);
        if (gameWon) {
            game.setStatus(Game.GameStatus.FINISHED);
            game.setWinnerId(playerId);
        } else {
            // Switch turns
            game.setCurrentPlayerId(opponentId);
        }

        gameRepository.save(game);

        return new MoveResult(isHit, gameWon, playerId);
    }

    private void validateShipPlacements(List<Ship> ships) throws Exception {
        Set<String> positions = new HashSet<>();

        for (Ship ship : ships) {
            if (ship.getRow() < 0 || ship.getRow() >= GRID_SIZE ||
                    ship.getCol() < 0 || ship.getCol() >= GRID_SIZE) {
                throw new Exception("Ship position out of bounds");
            }

            String position = ship.getRow() + "," + ship.getCol();
            if (positions.contains(position)) {
                throw new Exception("Ships cannot overlap");
            }
            positions.add(position);
        }
    }

    private List<Move> getPlayerMoves(Game game, Long playerId) throws JsonProcessingException {
        String movesJson = playerId.equals(game.getPlayer1Id()) ? game.getPlayer1Moves() : game.getPlayer2Moves();

        if (movesJson == null || movesJson.isEmpty()) {
            return new ArrayList<>();
        }

        return Arrays.asList(objectMapper.readValue(movesJson, Move[].class));
    }

    private List<Ship> getPlayerShips(Game game, Long playerId) throws JsonProcessingException {
        String shipsJson = playerId.equals(game.getPlayer1Id()) ? game.getPlayer1Ships() : game.getPlayer2Ships();

        if (shipsJson == null || shipsJson.isEmpty()) {
            return new ArrayList<>();
        }

        return Arrays.asList(objectMapper.readValue(shipsJson, Ship[].class));
    }

    private void savePlayerMoves(Game game, Long playerId, List<Move> moves) throws JsonProcessingException {
        String movesJson = objectMapper.writeValueAsString(moves);

        if (playerId.equals(game.getPlayer1Id())) {
            game.setPlayer1Moves(movesJson);
        } else {
            game.setPlayer2Moves(movesJson);
        }
    }

    private boolean checkWinCondition(List<Move> playerMoves, List<Ship> opponentShips) {
        Set<String> hitPositions = playerMoves.stream()
                .filter(Move::isHit)
                .map(m -> m.getRow() + "," + m.getCol())
                .collect(HashSet::new, HashSet::add, HashSet::addAll);

        Set<String> shipPositions = opponentShips.stream()
                .map(s -> s.getRow() + "," + s.getCol())
                .collect(HashSet::new, HashSet::add, HashSet::addAll);

        return hitPositions.containsAll(shipPositions);
    }

    public Game getGameById(Long gameId, Long playerId) throws Exception {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new Exception("Game not found"));

        if (!playerId.equals(game.getPlayer1Id()) && !playerId.equals(game.getPlayer2Id())) {
            throw new Exception("Player not in this game");
        }

        return game;
    }

    public Map<String, Object> getGameBoard(Long gameId, Long playerId) throws Exception {
        Game game = getGameById(gameId, playerId);

        // Get player's own ships and moves
        List<Ship> playerShips = getPlayerShips(game, playerId);
        List<Move> playerMoves = getPlayerMoves(game, playerId);

        // Get opponent's moves (attacks on player)
        Long opponentId = playerId.equals(game.getPlayer1Id()) ? game.getPlayer2Id() : game.getPlayer1Id();
        List<Move> opponentMoves = getPlayerMoves(game, opponentId);

        return Map.of(
                "playerShips", playerShips,
                "playerMoves", playerMoves,
                "opponentAttacks", opponentMoves,
                "gridSize", GRID_SIZE,
                "shipsPerPlayer", SHIPS_PER_PLAYER);
    }

    public void leaveGame(Long gameId, Long playerId) throws Exception {
        Game game = getGameById(gameId, playerId);

        if (game.getStatus() == Game.GameStatus.IN_PROGRESS) {
            // If game is in progress, opponent wins
            Long opponentId = playerId.equals(game.getPlayer1Id()) ? game.getPlayer2Id() : game.getPlayer1Id();
            game.setWinnerId(opponentId);
            game.setStatus(Game.GameStatus.FINISHED);
            gameRepository.save(game);
        } else if (game.getStatus() == Game.GameStatus.WAITING_FOR_PLAYERS) {
            // If waiting for players, delete the game
            gameRepository.delete(game);
        }
    }

    // Helper classes
    public static class Ship {
        private int row;
        private int col;

        public Ship() {
        }

        public Ship(int row, int col) {
            this.row = row;
            this.col = col;
        }

        public int getRow() {
            return row;
        }

        public void setRow(int row) {
            this.row = row;
        }

        public int getCol() {
            return col;
        }

        public void setCol(int col) {
            this.col = col;
        }
    }

    public static class Move {
        private int row;
        private int col;
        private boolean hit;

        public Move() {
        }

        public Move(int row, int col, boolean hit) {
            this.row = row;
            this.col = col;
            this.hit = hit;
        }

        public int getRow() {
            return row;
        }

        public void setRow(int row) {
            this.row = row;
        }

        public int getCol() {
            return col;
        }

        public void setCol(int col) {
            this.col = col;
        }

        public boolean isHit() {
            return hit;
        }

        public void setHit(boolean hit) {
            this.hit = hit;
        }
    }

    public static class MoveResult {
        private boolean hit;
        private boolean gameWon;
        private Long winnerId;

        public MoveResult(boolean hit, boolean gameWon, Long winnerId) {
            this.hit = hit;
            this.gameWon = gameWon;
            this.winnerId = winnerId;
        }

        public boolean isHit() {
            return hit;
        }

        public boolean isGameWon() {
            return gameWon;
        }

        public Long getWinnerId() {
            return winnerId;
        }
    }
}