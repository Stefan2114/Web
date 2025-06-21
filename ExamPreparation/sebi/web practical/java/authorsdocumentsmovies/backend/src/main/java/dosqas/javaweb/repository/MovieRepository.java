package dosqas.javaweb.repository;

import dosqas.javaweb.model.Author;
import dosqas.javaweb.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Integer> {
}