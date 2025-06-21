package dosqas.javaweb.service;

import dosqas.javaweb.model.FamilyRelation;
import dosqas.javaweb.repository.FamilyRelationRepository;
import dosqas.javaweb.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AppService {
    private final UserRepository userRepository;
    private final FamilyRelationRepository familyRelationRepository;

    // runtime injection
    public AppService(UserRepository userRepository, FamilyRelationRepository familyRelationRepository) {
        this.userRepository = userRepository;
        this.familyRelationRepository = familyRelationRepository;
    }

    public Boolean authenticate(String username, String parentName) {
        FamilyRelation familyRelation = familyRelationRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Username not found"));

        return familyRelation.getMother().equals(parentName) || familyRelation.getFather().equals(parentName);
    }

    public List<String> getFatherLine(String username) {
        FamilyRelation currentRelation = familyRelationRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Username not found"));

        List<String> result = new ArrayList<>();
        while (currentRelation.getFather() != null && !currentRelation.getFather().isEmpty()) {
            currentRelation = familyRelationRepository.findByUsername(currentRelation.getFather())
                    .orElseThrow(() -> new RuntimeException("Username not found"));
            result.add(currentRelation.getUsername());
        }

        return result;
    }

    public List<String> getMotherLine(String username) {
        FamilyRelation currentRelation = familyRelationRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Username not found"));

        List<String> result = new ArrayList<>();
        while (currentRelation.getMother() != null && !currentRelation.getMother().isEmpty()) {
            currentRelation = familyRelationRepository.findByUsername(currentRelation.getMother())
                    .orElseThrow(() -> new RuntimeException("Username not found"));
            result.add(currentRelation.getUsername());
        }

        return result;
    }
}
