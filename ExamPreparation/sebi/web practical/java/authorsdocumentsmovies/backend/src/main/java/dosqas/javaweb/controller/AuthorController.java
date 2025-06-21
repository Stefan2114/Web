package dosqas.javaweb.controller;

import dosqas.javaweb.dto.AuthoredWorkDTO;
import dosqas.javaweb.model.Document;
import dosqas.javaweb.service.AuthorService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/author")
public class AuthorController {
    private final AuthorService authorService;

    // used for dependency injection at runtime by spring
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping("/authorize")
    public Boolean checkAuthor(@RequestParam String authorName, @RequestParam Integer authoredId) {
        return authorService.checkAuthor(authorName, authoredId);
    }

    @PostMapping("/add")
    public Boolean addDocument(@RequestParam String authorName, @RequestParam String documentName, @RequestParam String contents) {
        return authorService.addDocument(authorName, documentName, contents);
    }

    @GetMapping("/interleaved")
    public List<AuthoredWorkDTO> getInterleaved(@RequestParam String authorName) {
        return authorService.getInterleavedDocumentsAndMovies(authorName);
    }

    @GetMapping("/largestCount")
    public Document getMostAuthored() {
        return authorService.getDocumentWithMostAuthors();
    }

    @DeleteMapping("/remove")
    public Boolean deleteMovie(@RequestParam String authorName, @RequestParam Integer movieId) {
        return authorService.deleteMovie(authorName, movieId);
    }
}