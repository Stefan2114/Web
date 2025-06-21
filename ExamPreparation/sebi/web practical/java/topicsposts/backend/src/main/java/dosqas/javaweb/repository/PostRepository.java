package dosqas.javaweb.repository;

import dosqas.javaweb.model.Topic;
import dosqas.javaweb.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Integer> {
    Optional<Post> findById(Integer id);
}