package com.example.lab9.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.example.lab9.DTO.FeedbackResponse;
import com.example.lab9.DTO.LoginRequest;
import com.example.lab9.DTO.SessionInfo;
import com.example.lab9.model.Customer;
import com.example.lab9.model.Feedback;
import com.example.lab9.service.AppService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api")
@SessionAttributes({ "currentCustomer", "flaggedFeedbacks", "flaggedCount" })
public class FeedbackController {

    private final AppService appService;

    public FeedbackController(AppService appService) {
        this.appService = appService;
    }

    @ModelAttribute("currentCustomer")
    public Customer currentCustomer() {
        return null;
    }

    @ModelAttribute("flaggedFeedbacks")
    public Set<Integer> flaggedFeedbacks() {
        return new HashSet<>();
    }

    @ModelAttribute("flaggedCount")
    public Integer flaggedCount() {
        return 0;
    }

    @PostMapping("/login")
    public ResponseEntity<SessionInfo> login(@RequestBody LoginRequest request, HttpSession session, Model model) {

        try {
            Customer customer = appService.findOrCreaCustomer(request.name(), request.email());
            Set<Integer> flaggedFeedbacks = new HashSet<>();
            Integer flaggedCount = 0;

            model.addAttribute("currentCustomer", customer);
            model.addAttribute("flaggedFeedbacks", flaggedFeedbacks);
            model.addAttribute("flaggedCount", flaggedCount);

            SessionInfo sessionInfo = new SessionInfo(customer, flaggedFeedbacks, flaggedCount);
            return ResponseEntity.ok(sessionInfo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

    }

    @GetMapping("/session")
    public ResponseEntity<FeedbackResponse> getSession() {
    }

    @PostMapping("/feedback")
    public ResponseEntity<String> submitFeedback(@RequestBody String text,
            @ModelAttribute("currentCustomer") Customer customer,
            @ModelAttribute("flaggedFeedbacks") Set<Integer> flaggedFeedbacks,
            @ModelAttribute("flaggedCount") Integer flaggedCount, Model model) {

        if (flaggedCount > 2) {
            return ResponseEntity.badRequest().body("Limit of flagged");
        }

        try {
            List<String> blockedWords = appService.checkForBlockedWords(text);
            Feedback feedback = appService.saveFeedback(customer.getId(), text);

            boolean isFlagged = !blockedWords.isEmpty();

            if (isFlagged) {
                flaggedFeedbacks.add(feedback.getId());
                Integer newCount = flaggedCount + 1;
                model.addAttribute("flaggedCount", newCount);

                return ResponseEntity.badRequest().body("flag words: " + blockedWords.toString());
            } else {
                return ResponseEntity.ok("Saved");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed");
        }
    }

    @PutMapping("/feedback/{id}")
    public ResponseEntity<String> updateFeedback(@PathVariable Integer id, @RequestBody String text,
            @ModelAttribute("currentCustomer") Customer customer,
            @ModelAttribute("flaggedFeedbacks") Set<Integer> flaggedFeedbacks,
            Model model) {

    }

}
