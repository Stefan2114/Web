package dosqas.javaweb.repository;

import dosqas.javaweb.model.BlockedWords;
import dosqas.javaweb.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {
}
