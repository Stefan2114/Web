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

@WebServlet(name = "loginServlet", urlPatterns = "/login")
public class LoginServlet extends HttpServlet {

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

        System.out.println("I am in doPost in loginServlet");
        // Set CORS headers for all responses
        setCorsHeaders(resp);

        try {

            // Your existing login logic here...
            User user = objectMapper.readValue(req.getInputStream(), User.class);
            if (user.getUsername() == null || user.getPassword() == null) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.setContentType("application/json");
                resp.getWriter().write("{\"success\": false, \"message\": \"Missing username or password\"}");
                return;
            }

            User dbUser = userRepository.findByUsername(user.getUsername()).orElse(null);
            if (dbUser != null && dbUser.getPassword().equals(user.getPassword())) {
                req.getSession(true).setAttribute("userId", dbUser.getId());
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.setContentType("application/json");
                resp.getWriter().write("{\"success\": true, \"message\": \"Login successful\", \"user\": {\"id\": " +
                        dbUser.getId() + ", \"username\": \"" + dbUser.getUsername() + "\"}}");
            } else {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                resp.setContentType("application/json");
                resp.getWriter().write("{\"success\": false, \"message\": \"Invalid credentials\"}");
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setContentType("application/json");
            resp.getWriter().write("{\"success\": false, \"message\": \"Error processing login\"}");
        }
    }
}