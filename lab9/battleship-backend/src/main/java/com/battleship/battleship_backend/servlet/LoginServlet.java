package com.battleship.battleship_backend.servlet;

import com.battleship.battleship_backend.entity.User;
import com.battleship.battleship_backend.service.UserService;
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
import java.util.Map;

@Component
@WebServlet(name = "LoginServlet", urlPatterns = { "/servlet/login" })
public class LoginServlet extends HttpServlet {

    @Autowired
    private UserService userService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            // Read JSON from request body
            StringBuilder jsonBuffer = new StringBuilder();
            String line;
            while ((line = request.getReader().readLine()) != null) {
                jsonBuffer.append(line);
            }

            // Parse JSON
            Map<String, String> loginData = objectMapper.readValue(jsonBuffer.toString(), Map.class);
            String username = loginData.get("username");
            String password = loginData.get("password");

            // Validate input
            if (username == null || username.trim().isEmpty()) {
                sendErrorResponse(response, "Username is required");
                return;
            }

            if (password == null || password.trim().isEmpty()) {
                sendErrorResponse(response, "Password is required");
                return;
            }

            // Authenticate user
            User user = userService.authenticate(username, password);

            // Create session
            HttpSession session = request.getSession();
            session.setAttribute("userId", user.getId());
            session.setAttribute("username", user.getUsername());

            // Send success response
            Map<String, Object> responseData = Map.of(
                    "success", true,
                    "message", "Login successful",
                    "user", Map.of(
                            "id", user.getId(),
                            "username", user.getUsername()));

            response.getWriter().write(objectMapper.writeValueAsString(responseData));

        } catch (Exception e) {
            sendErrorResponse(response, e.getMessage());
        }
    }

    private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        Map<String, Object> errorResponse = Map.of(
                "success", false,
                "message", message);
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}