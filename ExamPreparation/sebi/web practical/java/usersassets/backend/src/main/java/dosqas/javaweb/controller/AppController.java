package dosqas.javaweb.controller;

import dosqas.javaweb.dto.AddAssetsRequest;
import dosqas.javaweb.model.Asset;
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
    public Boolean authorizeUser(@RequestParam String username, @RequestParam String challenge) {
        return appService.authenticate(username, challenge);
    }

    @GetMapping("/getAssets")
    public List<Asset> getAssets(@RequestParam String username) {
        return appService.getAssets(username);
    }

    @PostMapping("/addAssets")
    public Boolean addAssets(@RequestBody AddAssetsRequest request) {
        return appService.addAssets(request.username, request.assets);
    }
}