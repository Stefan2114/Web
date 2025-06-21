package com.battleships.battleships_backend.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "logoutServlet", urlPatterns = "/api/logout")
public class LogoutServlet extends HttpServlet {

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

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "Logged out successfully");

        objectMapper.writeValue(response.getWriter(), result);
    }
}
