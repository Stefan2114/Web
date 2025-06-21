package dosqas.javaweb.controller;

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
    public Boolean checkAuthor(@RequestParam String username, @RequestParam String challenge) {
        return appService.authenticate(username, challenge);
    }

    @GetMapping("/getFathers")
    public List<String> getFathers(@RequestParam String username) {
        return appService.getFatherLine(username);
    }

    @GetMapping("/getMothers")
    public List<String> getMothers(@RequestParam String username) {
        return appService.getMotherLine(username);
    }
}