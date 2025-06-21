package dosqas.javaweb.controller;

import dosqas.javaweb.dto.PostDataDTO;
import dosqas.javaweb.model.Post;
import dosqas.javaweb.service.PostService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    // used for dependency injection at runtime by spring
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public List<PostDataDTO> getPosts() {
        return postService.getAllPosts();
    }

    @PostMapping
    public Post addPost(@RequestParam String user, @RequestParam String topicName, @RequestParam String text) {
        return postService.addPost(user, topicName, text);
    }

    @PatchMapping
    public Post modifyPost(@RequestParam Integer id, @RequestParam String user, @RequestParam String topicName, @RequestParam String text) {
        return postService.modifyPost(id, user, topicName, text);
    }
}