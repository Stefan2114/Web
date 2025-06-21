package com.battleships.battleships_backend.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.battleships.battleships_backend.model.Game;
import com.battleships.battleships_backend.repository.GameRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "gameServlet", urlPatterns = "/api/game/*")
public class GameServlet extends HttpServlet {

    @Autowired
    private GameRepository gameRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    private void setCorsHeaders(HttpServletResponse resp) {
        resp.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
        resp.setHeader("Access-Control-Allow-Credentials", "true");
        resp.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept");
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Handle preflight requests
        setCorsHeaders(resp);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        setCorsHeaders(response);

        String username = getAuthenticatedUser(request, response);
        if (username == null)
            return;

        String pathInfo = request.getPathInfo();

        if ("/join".equals(pathInfo)) {
            joinGame(username, response);
        } else if ("/status".equals(pathInfo)) {
            getGameStatus(username, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        setCorsHeaders(response);

        String username = getAuthenticatedUser(request, response);
        if (username == null)
            return;

        String pathInfo = request.getPathInfo();

        if ("/setup".equals(pathInfo)) {
            setupShips(username, request, response);
        } else if ("/attack".equals(pathInfo)) {
            makeAttack(username, request, response);
        }
    }

    private String getAuthenticatedUser(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            response.setStatus(401);
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Not authenticated");
            objectMapper.writeValue(response.getWriter(), error);
            return null;
        }
        return (String) session.getAttribute("username");
    }

    private void joinGame(String username, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        Map<String, Object> result = new HashMap<>();

        // Check if player already has an active game
        Game activeGame = gameRepository.findActiveGameByPlayer(username);
        if (activeGame != null) {
            result.put("success", true);
            result.put("gameId", activeGame.getId());
            result.put("message", "Rejoined existing game");
            objectMapper.writeValue(response.getWriter(), result);
            return;
        }

        // Look for waiting game
        Game waitingGame = gameRepository.findWaitingGame();

        if (waitingGame == null) {
            // Create new game
            Game newGame = new Game();
            newGame.setPlayer1Username(username);
            newGame.setGameStatus("WAITING");
            newGame.setCurrentPlayer(username);
            gameRepository.save(newGame);

            result.put("success", true);
            result.put("gameId", newGame.getId());
            result.put("message", "Waiting for opponent");
        } else {
            // Join existing game
            if (waitingGame.getPlayer1Username().equals(username)) {
                result.put("success", false);
                result.put("message", "Cannot join your own game");
            } else {
                waitingGame.setPlayer2Username(username);
                waitingGame.setGameStatus("ACTIVE");
                gameRepository.save(waitingGame);

                result.put("success", true);
                result.put("gameId", waitingGame.getId());
                result.put("message", "Game started");
            }
        }

        objectMapper.writeValue(response.getWriter(), result);
    }

    private void getGameStatus(String username, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        Game game = gameRepository.findActiveGameByPlayer(username);
        Map<String, Object> result = new HashMap<>();

        if (game == null) {
            result.put("success", false);
            result.put("message", "No active game");
        } else {
            result.put("success", true);
            result.put("gameId", game.getId());
            result.put("player1", game.getPlayer1Username());
            result.put("player2", game.getPlayer2Username());
            result.put("currentPlayer", game.getCurrentPlayer());
            result.put("gameStatus", game.getGameStatus());
            result.put("winner", game.getWinner());

            // Return appropriate grid data
            if (username.equals(game.getPlayer1Username())) {
                result.put("myGrid", game.getPlayer1Grid());
                result.put("myAttacks", game.getPlayer1Attacks());
            } else {
                result.put("myGrid", game.getPlayer2Grid());
                result.put("myAttacks", game.getPlayer2Attacks());
            }
        }

        objectMapper.writeValue(response.getWriter(), result);
    }

    private void setupShips(String username, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("application/json");

        Map<String, String> setupData = objectMapper.readValue(request.getReader(), Map.class);
        String gridData = setupData.get("grid");

        Game game = gameRepository.findActiveGameByPlayer(username);
        Map<String, Object> result = new HashMap<>();

        if (game == null) {
            result.put("success", false);
            result.put("message", "No active game");
        } else {
            if (username.equals(game.getPlayer1Username())) {
                game.setPlayer1Grid(gridData);
            } else {
                game.setPlayer2Grid(gridData);
            }
            gameRepository.save(game);

            result.put("success", true);
            result.put("message", "Ships positioned");
        }

        objectMapper.writeValue(response.getWriter(), result);
    }

    private void makeAttack(String username, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("application/json");

        Map<String, Object> attackData = objectMapper.readValue(request.getReader(), Map.class);
        int row = (Integer) attackData.get("row");
        int col = (Integer) attackData.get("col");

        Game game = gameRepository.findActiveGameByPlayer(username);
        Map<String, Object> result = new HashMap<>();

        if (game == null || !username.equals(game.getCurrentPlayer())) {
            result.put("success", false);
            result.put("message", "Not your turn");
        } else {
            // Process attack logic here
            boolean isPlayer1 = username.equals(game.getPlayer1Username());
            String targetGrid = isPlayer1 ? game.getPlayer2Grid() : game.getPlayer1Grid();
            String myAttacks = isPlayer1 ? game.getPlayer1Attacks() : game.getPlayer2Attacks();

            // Simple hit detection (assuming grid is comma-separated values)
            boolean hit = checkHit(targetGrid, row, col);
            String updatedAttacks = updateAttacks(myAttacks, row, col, hit);

            if (isPlayer1) {
                game.setPlayer1Attacks(updatedAttacks);
                game.setCurrentPlayer(game.getPlayer2Username());
            } else {
                game.setPlayer2Attacks(updatedAttacks);
                game.setCurrentPlayer(game.getPlayer1Username());
            }

            // Check for winner
            if (checkWinner(updatedAttacks)) {
                game.setWinner(username);
                game.setGameStatus("FINISHED");
            }

            gameRepository.save(game);

            result.put("success", true);
            result.put("hit", hit);
            result.put("gameOver", game.getGameStatus().equals("FINISHED"));
            result.put("winner", game.getWinner());
        }

        objectMapper.writeValue(response.getWriter(), result);
    }

    private boolean checkHit(String grid, int row, int col) {
        if (grid == null)
            return false;
        String[] cells = grid.split(",");
        int index = row * 5 + col; // Assuming 5x5 grid
        return index < cells.length && "1".equals(cells[index]);
    }

    private String updateAttacks(String attacks, int row, int col, boolean hit) {
        if (attacks == null) {
            attacks = "0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0";
        }
        String[] cells = attacks.split(",");
        int index = row * 5 + col;
        if (index < cells.length) {
            cells[index] = hit ? "2" : "1"; // 1=miss, 2=hit
        }
        return String.join(",", cells);
    }

    private boolean checkWinner(String attacks) {
        if (attacks == null)
            return false;
        String[] cells = attacks.split(",");
        int hits = 0;
        for (String cell : cells) {
            if ("2".equals(cell))
                hits++;
        }
        return hits >= 2; // Win when both ships are hit
    }
}
