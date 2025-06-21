package com.battleships.battleships_backend.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.battleships.battleships_backend.model.User;
import com.battleships.battleships_backend.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "loginServlet", urlPatterns = "/api/login")
public class LoginServlet extends HttpServlet {

    @Autowired
    private UserRepository userRepository;

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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        setCorsHeaders(response);

        try {
            Map<String, String> loginData = objectMapper.readValue(request.getReader(), Map.class);
            String username = loginData.get("username");
            String password = loginData.get("password");

            Map<String, Object> result = new HashMap<>();

            if (username == null || username.trim().isEmpty()) {
                result.put("success", false);
                result.put("message", "Username is required");
                response.setStatus(400);
                objectMapper.writeValue(response.getWriter(), result);
                return;
            }

            if (password == null || password.trim().isEmpty()) {
                result.put("success", false);
                result.put("message", "Password is required");
                response.setStatus(400);
                objectMapper.writeValue(response.getWriter(), result);
                return;
            }

            User user = userRepository.findByUsernameAndPassword(username, password);

            if (user != null) {
                HttpSession session = request.getSession();
                session.setAttribute("username", username);
                session.setMaxInactiveInterval(30 * 60); // 30 minutes

                result.put("success", true);
                result.put("username", username);
                result.put("message", "Login successful");
            } else {
                // Try to create new user if doesn't exist
                User existingUser = userRepository.findByUsername(username);
                if (existingUser == null) {
                    User newUser = new User(username, password);
                    userRepository.save(newUser);

                    HttpSession session = request.getSession();
                    session.setAttribute("username", username);
                    session.setMaxInactiveInterval(30 * 60);

                    result.put("success", true);
                    result.put("username", username);
                    result.put("message", "Account created and logged in");
                } else {
                    result.put("success", false);
                    result.put("message", "Invalid credentials");
                    response.setStatus(401);
                }
            }

            objectMapper.writeValue(response.getWriter(), result);

        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Server error");
            response.setStatus(500);
            objectMapper.writeValue(response.getWriter(), error);
        }
    }
}
