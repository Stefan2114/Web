package com.example.lab9.DTO;

import java.time.LocalDateTime;
import java.util.List;

public record FeedbackResponse(
        Integer id,
        Integer customerId,
        String message,
        LocalDateTime timestamp,
        boolean flagged,
        List<String> blockedWords) {

}