package dosqas.javaweb.repository;

import dosqas.javaweb.model.FamilyRelation;
import dosqas.javaweb.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FamilyRelationRepository extends JpaRepository<FamilyRelation, Integer> {
    Optional<FamilyRelation> findByUsername(String username);

}
