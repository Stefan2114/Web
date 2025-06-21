package dosqas.javaweb.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dosqas.javaweb.dto.AddAssetsRequest;
import dosqas.javaweb.model.Asset;
import dosqas.javaweb.model.User;
import dosqas.javaweb.repository.AssetRepository;
import dosqas.javaweb.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AppService {
    private final UserRepository userRepository;
    private final AssetRepository assetRepository;

    // runtime injection
    public AppService(UserRepository userRepository, AssetRepository assetRepository) {
        this.userRepository = userRepository;
        this.assetRepository = assetRepository;
    }

    public Boolean authenticate(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Username not found"));

        return user.getPassword().equals(password);
    }

    public List<Asset> getAssets(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Username not found"));

        return assetRepository.findByUserId(user.getId());
    }

    public Boolean addAssets(String username, List<AddAssetsRequest.AssetDto> assetDtos) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Username not found"));

        List<Asset> assets = new ArrayList<>();
        for (AddAssetsRequest.AssetDto dto : assetDtos) {
            Asset asset = new Asset();
            asset.setName(dto.name);
            asset.setDescription(dto.description);
            asset.setValue(dto.value);
            asset.setUser(user);
            assets.add(asset);
        }

        assetRepository.saveAll(assets);
        return true;
    }
}
