package com.example.lab9.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.lab9.DTO.LoginRequest;
import com.example.lab9.service.CustomerService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    private final CustomerService customerService;

    public AuthController(CustomerService customerService) {
        this.customerService = customerService;
    }

@PostMapping("/login")
public ResponseEntity<String> login(@RequestBody LoginRequest request,
HttpServletRequest httpRequest) {

System.out.println(request);
try {

if (!this.customerService.goodAuthentication(request.username(),
request.email())) {
return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid
credentials");
}

HttpSession session = httpRequest.getSession(true);
session.setAttribute("SPRING_SECURITY_CONTEXT", request.username());
session.setMaxInactiveInterval(30 * 60);
return ResponseEntity.ok("Login successful");

} catch (AuthenticationException exception) {
return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid
credentials");
}
}

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Logout successful");
    }
}
