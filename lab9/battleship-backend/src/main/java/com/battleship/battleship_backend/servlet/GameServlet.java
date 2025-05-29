package com.battleship.battleship_backend.servlet;

import com.battleship.battleship_backend.entity.Game;
import com.battleship.battleship_backend.service.GameService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
@WebServlet(name = "GameServlet", urlPatterns = { "/servlet/game/*" })
public class GameServlet extends HttpServlet {

    @Autowired
    private GameService gameService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String pathInfo = request.getPathInfo();

        try {
            Long userId = getUserIdFromSession(request);

            if ("/join".equals(pathInfo)) {
                handleJoinGame(userId, response);
            } else if (pathInfo != null && pathInfo.matches("/\\d+/ships")) {
                Long gameId = extractGameId(pathInfo);
                handlePlaceShips(gameId, userId, request, response);
            } else if (pathInfo != null && pathInfo.matches("/\\d+/move")) {
                Long gameId = extractGameId(pathInfo);
                handleMakeMove(gameId, userId, request, response);
            } else {
                sendErrorResponse(response, "Invalid endpoint");
            }

        } catch (Exception e) {
            sendErrorResponse(response, e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String pathInfo = request.getPathInfo();

        try {
            Long userId = getUserIdFromSession(request);

            if (pathInfo != null && pathInfo.matches("/\\d+")) {
                Long gameId = Long.parseLong(pathInfo.substring(1));
                handleGetGame(gameId, userId, response);
            } else if (pathInfo != null && pathInfo.matches("/\\d+/status")) {
                Long gameId = extractGameId(pathInfo);
                handleGetGameStatus(gameId, userId, response);
            } else if (pathInfo != null && pathInfo.matches("/\\d+/board")) {
                Long gameId = extractGameId(pathInfo);
                handleGetGameBoard(gameId, userId, response);
            } else {
                sendErrorResponse(response, "Invalid endpoint");
            }

        } catch (Exception e) {
            sendErrorResponse(response, e.getMessage());
        }
    }

    private void handleJoinGame(Long userId, HttpServletResponse response) throws Exception, IOException {
        Game game = gameService.joinOrCreateGame(userId);

        Map<String, Object> responseData = Map.of(
                "success", true,
                "game", gameToMap(game));

        response.getWriter().write(objectMapper.writeValueAsString(responseData));
    }

    private void handlePlaceShips(Long gameId, Long userId, HttpServletRequest request,
            HttpServletResponse response) throws Exception, IOException {

        // Read JSON from request body
        StringBuilder jsonBuffer = new StringBuilder();
        String line;
        while ((line = request.getReader().readLine()) != null) {
            jsonBuffer.append(line);
        }

        // Parse ships
        GameService.Ship[] shipsArray = objectMapper.readValue(jsonBuffer.toString(), GameService.Ship[].class);
        List<GameService.Ship> ships = List.of(shipsArray);

        Game game = gameService.placeShips(gameId, userId, ships);

        Map<String, Object> responseData = Map.of(
                "success", true,
                "game", gameToMap(game));

        response.getWriter().write(objectMapper.writeValueAsString(responseData));
    }

    private void handleMakeMove(Long gameId, Long userId, HttpServletRequest request,
            HttpServletResponse response) throws Exception, IOException {

        // Read JSON from request body
        StringBuilder jsonBuffer = new StringBuilder();
        String line;
        while ((line = request.getReader().readLine()) != null) {
            jsonBuffer.append(line);
        }

        // Parse move
        Map<String, Integer> moveData = objectMapper.readValue(jsonBuffer.toString(), Map.class);
        int row = moveData.get("row");
        int col = moveData.get("col");

        GameService.MoveResult result = gameService.makeMove(gameId, userId, row, col);

        Map<String, Object> responseData = Map.of(
                "success", true,
                "hit", result.isHit(),
                "gameWon", result.isGameWon(),
                "winnerId", result.getWinnerId() != null ? result.getWinnerId() : "");

        response.getWriter().write(objectMapper.writeValueAsString(responseData));
    }

    private void handleGetGame(Long gameId, Long userId, HttpServletResponse response)
            throws Exception, IOException {

        Game game = gameService.getGameById(gameId, userId);

        Map<String, Object> responseData = Map.of(
                "success", true,
                "game", gameToMap(game));

        response.getWriter().write(objectMapper.writeValueAsString(responseData));
    }

    private void handleGetGameStatus(Long gameId, Long userId, HttpServletResponse response)
            throws Exception, IOException {

        Game game = gameService.getGameById(gameId, userId);

        Map<String, Object> responseData = Map.of(
                "success", true,
                "status", game.getStatus().toString(),
                "currentPlayer", game.getCurrentPlayerId(),
                "isYourTurn", userId.equals(game.getCurrentPlayerId()));

        response.getWriter().write(objectMapper.writeValueAsString(responseData));
    }

    private void handleGetGameBoard(Long gameId, Long userId, HttpServletResponse response)
            throws Exception, IOException {

        Map<String, Object> boardData = gameService.getGameBoard(gameId, userId);

        Map<String, Object> responseData = Map.of(
                "success", true,
                "board", boardData);

        response.getWriter().write(objectMapper.writeValueAsString(responseData));
    }

    private Long getUserIdFromSession(HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new Exception("No session found");
        }

        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            throw new Exception("User not authenticated");
        }

        return userId;
    }

    private Long extractGameId(String pathInfo) {
        String[] parts = pathInfo.split("/");
        return Long.parseLong(parts[1]);
    }

    private Map<String, Object> gameToMap(Game game) {
        return Map.of(
                "id", game.getId(),
                "player1Id", game.getPlayer1Id(),
                "player2Id", game.getPlayer2Id() != null ? game.getPlayer2Id() : "",
                "currentPlayerId", game.getCurrentPlayerId() != null ? game.getCurrentPlayerId() : "",
                "status", game.getStatus().toString(),
                "winnerId", game.getWinnerId() != null ? game.getWinnerId() : "",
                "createdAt", game.getCreatedAt().toString(),
                "updatedAt", game.getUpdatedAt().toString());
    }

    private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        Map<String, Object> errorResponse = Map.of(
                "success", false,
                "message", message);
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}