package com.example.lab9.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.lab9.DTO.FeedbackAddingRequest;
import com.example.lab9.DTO.FeedbackAddingResponse;
import com.example.lab9.DTO.LoginRequest;
import com.example.lab9.model.Feedback;
import com.example.lab9.service.AppService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AppController {
    private final AppService appService;

    // used for dependency injection at runtime by spring
    public AppController(AppService appService) {
        this.appService = appService;
    }

    @PostMapping("/authorize")
    public Boolean authorizeUser(@RequestParam String username, @RequestParam String email) {
        return appService.authenticate(username, email);
    }

    @GetMapping("/getFeedback")
    public List<Feedback> getFeedback() {
        return appService.getFeedback();
    }

    @PostMapping("/addFeedback")
    public FeedbackAddingResponse addFeedback(@RequestBody FeedbackAddingRequest request) {
        return appService.addFeedback(request);
    }

    @GetMapping("/cleanMessage")
    public String cleanMessage(String message) {
        return appService.cleanMessage(message);
    }
}