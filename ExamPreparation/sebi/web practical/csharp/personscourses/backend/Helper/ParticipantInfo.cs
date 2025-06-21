using System.Text.Json.Serialization;

public class ParticipantInfo
{
    [JsonPropertyName("name")]
    public string Name { get; set; } = "";
    
    [JsonPropertyName("grade")]
    public int Grade { get; set; }
}