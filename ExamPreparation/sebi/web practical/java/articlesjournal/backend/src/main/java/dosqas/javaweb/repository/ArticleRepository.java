package dosqas.javaweb.repository;

import dosqas.javaweb.model.Article;
import dosqas.javaweb.model.Journal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// This interface is a repository for the Article entity with Integer as the type of its primary key.
// At runtime, Spring will inject the implementation for this repository.
// It also automatically generates the implementation for the methods defined here.
public interface ArticleRepository extends JpaRepository<Article, Integer> {
    List<Article> findByUserAndJournal(String user, Journal journal);
}