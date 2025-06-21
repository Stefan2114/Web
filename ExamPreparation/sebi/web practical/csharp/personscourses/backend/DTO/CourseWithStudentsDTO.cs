using System.Text.Json.Serialization;

public class CourseWithParticipantsDTO
{
    public int Id { get; set; }
    public string ProfessorName { get; set; } = "";
    public string CourseName { get; set; } = "";
    public List<ParticipantInfo>? ParticipantsAndGrades { get; set; }
}