package com.battleships.battleships_backend.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;

import com.battleships.battleships_backend.entity.User;
import com.battleships.battleships_backend.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebServlet(name = "registerServlet", urlPatterns = "/register")
public class RegisterServlet extends HttpServlet {

    @Autowired
    private UserRepository userRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Set CORS headers for all responses
        setCorsHeaders(resp);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            // Deserialize User from JSON request body
            User user = objectMapper.readValue(req.getInputStream(), User.class);

            if (user.getUsername() == null || user.getPassword() == null) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"success\": false, \"message\": \"Missing username or password\"}");
                return;
            }

            // Check if user already exists
            if (userRepository.findByUsername(user.getUsername()).isPresent()) {
                resp.setStatus(HttpServletResponse.SC_CONFLICT);
                resp.getWriter().write("{\"success\": false, \"message\": \"Username already exists\"}");
                return;
            }

            // Save user
            User savedUser = userRepository.save(user);

            // Create session for the new user
            req.getSession(true).setAttribute("userId", savedUser.getId());

            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter()
                    .write("{\"success\": true, \"message\": \"User registered successfully\", \"user\": {\"id\": " +
                            savedUser.getId() + ", \"username\": \"" + savedUser.getUsername() + "\"}}");

        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(
                    "{\"success\": false, \"message\": \"Error processing registration: " + e.getMessage() + "\"}");
        }
    }
}