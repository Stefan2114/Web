package com.example.lab9.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.lab9.DTO.UpdatedTaskDTO;
import com.example.lab9.model.Task;
import com.example.lab9.service.AppService;

@RestController
@RequestMapping("/api")
public class AppController {
    private final AppService appService;

    // used for dependency injection at runtime by spring
    public AppController(AppService appService) {
        this.appService = appService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> loginUser(@RequestBody String username) {
        System.out.println("I got here");
        if (appService.login(username)) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.badRequest().build();
    }

    @GetMapping("tasks")
    public ResponseEntity<List<Task>> getTasks() {
        System.out.println("I got here too");
        return ResponseEntity.ok(this.appService.getTasks());
    }

    @PutMapping("tasks")
    public ResponseEntity<Void> updateTaskStatus(@RequestBody UpdatedTaskDTO task) {

        System.out.println("Try to update");
        this.appService.updateTaskStatus(task);
        return ResponseEntity.noContent().build();

    }

    // @GetMapping("/getFeedback")
    // public List<Feedback> getFeedback() {
    // return appService.getFeedback();
    // }

    // @PostMapping("/addFeedback")
    // public FeedbackAddingResponse addFeedback(@RequestBody FeedbackAddingRequest
    // request) {
    // return appService.addFeedback(request);
    // }

    // @GetMapping("/cleanMessage")
    // public String cleanMessage(String message) {
    // return appService.cleanMessage(message);
    // }
}