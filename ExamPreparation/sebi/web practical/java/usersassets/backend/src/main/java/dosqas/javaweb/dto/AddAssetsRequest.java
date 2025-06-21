package dosqas.javaweb.dto;

import lombok.Data;

import java.util.List;

@Data
public class AddAssetsRequest {
    public String username;
    public List<AssetDto> assets;

    public static class AssetDto {
        public String name;
        public String description;
        public int value;
    }
}
