using System.Text.Json.Serialization;

public class GradeAssignRequest
{
    [JsonPropertyName("courseName")]
    public string? CourseName { get; set; }
    
    [JsonPropertyName("professorName")]
    public string? ProfessorName { get; set; } = "";
    
    [JsonPropertyName("participantName")]
    public string ParticipantName { get; set; } = "";
    
    [JsonPropertyName("participantGrade")]
    public int ParticipantGrade { get; set; }
}