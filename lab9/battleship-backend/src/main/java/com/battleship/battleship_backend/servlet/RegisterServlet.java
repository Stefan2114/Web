package com.battleship.battleship_backend.servlet;

import com.battleship.battleship_backend.entity.User;
import com.battleship.battleship_backend.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
@WebServlet(name = "RegisterServlet", urlPatterns = { "/servlet/register" })
public class RegisterServlet extends HttpServlet {

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
            Map<String, String> registerData = objectMapper.readValue(jsonBuffer.toString(), Map.class);
            String username = registerData.get("username");
            String password = registerData.get("password");

            // Register user
            User user = userService.register(username, password);

            // Send success response
            Map<String, Object> responseData = Map.of(
                    "success", true,
                    "message", "Registration successful",
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
