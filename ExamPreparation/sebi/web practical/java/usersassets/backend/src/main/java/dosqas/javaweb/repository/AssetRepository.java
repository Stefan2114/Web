package dosqas.javaweb.repository;

import dosqas.javaweb.model.Asset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AssetRepository extends JpaRepository<Asset, Integer> {
    List<Asset> findByUserId(Integer userId);
}