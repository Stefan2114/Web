package dosqas.javaweb.controller;

import dosqas.javaweb.dto.FeedbackAddingRequest;
import dosqas.javaweb.dto.FeedbackAddingResponse;
import dosqas.javaweb.model.Feedback;
import dosqas.javaweb.service.AppService;
import org.springframework.web.bind.annotation.*;

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