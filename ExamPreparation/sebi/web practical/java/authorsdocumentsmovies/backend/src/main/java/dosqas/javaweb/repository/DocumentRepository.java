package dosqas.javaweb.repository;

import dosqas.javaweb.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DocumentRepository extends JpaRepository<Document, Integer> {
    Optional<Document> findById(Integer id);
}