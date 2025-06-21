package dosqas.javaweb.controller;

import dosqas.javaweb.model.Article;
import dosqas.javaweb.service.ArticleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/articles")
public class ArticleController {
    private final ArticleService articleService;

    // used for dependency injection at runtime by spring
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping
    // mapped to GET for my REST endpoint
    // requestparam - put in the request url as a param, not in the body of it
    public List<Article> getArticles(@RequestParam String user, @RequestParam String journalName) {
        return articleService.getArticlesByUserAndJournal(user, journalName);
    }

    @PostMapping
    public Article addArticle(@RequestParam String user, @RequestParam String journalName, @RequestParam String summary) {
        return articleService.addArticle(user, journalName, summary);
    }
}