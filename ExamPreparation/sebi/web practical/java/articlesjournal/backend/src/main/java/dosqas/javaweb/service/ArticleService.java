package dosqas.javaweb.service;

import dosqas.javaweb.dto.ArticleNotification;
import dosqas.javaweb.model.Article;
import dosqas.javaweb.model.Journal;
import dosqas.javaweb.repository.ArticleRepository;
import dosqas.javaweb.repository.JournalRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final JournalRepository journalRepository;
    // simp = simple messaging protocol
    private final SimpMessagingTemplate messagingTemplate;

    // runtime injection
    public ArticleService(ArticleRepository articleRepository, JournalRepository journalRepository, SimpMessagingTemplate messagingTemplate) {
        this.articleRepository = articleRepository;
        this.journalRepository = journalRepository;
        this.messagingTemplate = messagingTemplate;
    }

    public List<Article> getArticlesByUserAndJournal(String user, String journalName) {
        Journal journal = journalRepository.findByName(journalName)
                .orElseThrow(() -> new IllegalArgumentException("Journal not found"));
        return articleRepository.findByUserAndJournal(user, journal);
    }

    public Article addArticle(String user, String journalName, String summary) {
        Journal journal = journalRepository.findByName(journalName)
                .orElseGet(() -> {
                    Journal newJournal = new Journal();
                    newJournal.setName(journalName);
                    return journalRepository.save(newJournal);
                });

        Article article = new Article();
        article.setUser(user);
        article.setJournal(journal);
        article.setSummary(summary);
        article.setDate(LocalDateTime.now());
        
        Article savedArticle = articleRepository.save(article);
        
        // Send WebSocket notification to all connected clients
        ArticleNotification notification = new ArticleNotification(
            savedArticle.getId(),
            savedArticle.getUser(),
            savedArticle.getJournal().getName(),
            savedArticle.getSummary(),
            savedArticle.getDate()
        );

        // auto converts objects to json
        // designed for websocket communication
        messagingTemplate.convertAndSend("/topic/articles", notification);
        
        return savedArticle;
    }
}