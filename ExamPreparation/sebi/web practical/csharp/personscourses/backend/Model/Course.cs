using Microsoft.EntityFrameworkCore.Migrations.Operations;

public class Course
{
    public int Id { get; set; }
    public int ProfessorId { get; set; }
    public string CourseName { get; set; } = "";
    public string Participants { get; set; } = "";
    public string Grades { get; set; } = "";
}