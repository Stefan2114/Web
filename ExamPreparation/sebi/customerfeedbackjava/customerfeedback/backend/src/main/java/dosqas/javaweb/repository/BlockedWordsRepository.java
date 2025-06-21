package dosqas.javaweb.repository;

import dosqas.javaweb.model.BlockedWords;
import dosqas.javaweb.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlockedWordsRepository extends JpaRepository<BlockedWords, Integer> {
}