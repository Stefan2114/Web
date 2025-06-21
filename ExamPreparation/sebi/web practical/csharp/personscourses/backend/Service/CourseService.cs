using System.Text.Json;
using System.Text.Json.Serialization;
using Microsoft.EntityFrameworkCore;

public class CourseService
{
    private readonly AppDbContext _context;

    public CourseService(AppDbContext context)
    {
        _context = context;
    } 

    private List<ParticipantInfo> DeserializeParticipants(string json)
    {
        if (string.IsNullOrEmpty(json) || json.Trim() == "[]" || json.Trim() == "null")
            return new List<ParticipantInfo>();

        try
        {
            return JsonSerializer.Deserialize<List<ParticipantInfo>>(json) ?? new List<ParticipantInfo>();
        }
        catch (JsonException)
        {
            return new List<ParticipantInfo>();
        }
    }

    public async Task<List<CourseWithParticipantsDTO>> GetCoursesAsync()
    {
        var courses = await _context.Courses.ToListAsync();
        var result = new List<CourseWithParticipantsDTO>();

        foreach (var course in courses)
        {
            var professor = await _context.Persons.FirstOrDefaultAsync(p => p.Id == course.ProfessorId);

            var courseDTO = new CourseWithParticipantsDTO
            {
                Id = course.Id,
                ProfessorName = professor?.Name ?? "",
                CourseName = course.CourseName,
                ParticipantsAndGrades = DeserializeParticipants(course.Participants),
            };

            result.Add(courseDTO);
        }

        return result.ToList();
    }

    public async Task<List<CourseWithParticipantsDTO>> GetCoursesForParticipantAsync(string participantName)
    {
        var courses = await _context.Courses.ToListAsync();
        var result = new List<CourseWithParticipantsDTO>();

        foreach (var course in courses)
        {
            var participantsAndGrades = DeserializeParticipants(course.Grades);

            if (!participantsAndGrades.Any(s => s.Name == participantName)) continue;

            var professor = await _context.Persons.FirstOrDefaultAsync(p => p.Id == course.ProfessorId);

            var courseDTO = new CourseWithParticipantsDTO
            {
                Id = course.Id,
                ProfessorName = professor?.Name ?? "",
                CourseName = course.CourseName,
                ParticipantsAndGrades = DeserializeParticipants(course.Participants),
            };

            result.Add(courseDTO);
        }

        return result.ToList();
    }

    public async Task<(bool Success, string Message)> AssignGradeToParticipantAsync(string courseName, string professorName, string participantName, int participantGrade)
    {
        // Find the course
        var course = await _context.Courses.FirstOrDefaultAsync(c => c.CourseName == courseName);
        if (course == null)
        {
            return (false, "Course not found!");
        }

        // Find the professor
        var professor = await _context.Persons.FirstOrDefaultAsync(p => p.Id == course.ProfessorId);
        if (professor == null)
        {
            return (false, "Professor not found!");
        }

        // Check if the professor name matches
        if (professor.Name != professorName)
        {
            return (false, $"Professor name '{professorName}' does not match the course professor '{professor.Name}'!");
        }

        // Deserialize existing participants and grades
        var participantsAndGrades = DeserializeParticipants(course.Grades);

        // Find existing participant
        var existing = participantsAndGrades.FirstOrDefault(s => s.Name == participantName);
        if (existing != null)
        {
            // Update existing grade
            existing.Grade = participantGrade;
        }
        else
        {
            // Add new participant with grade
            participantsAndGrades.Add(new ParticipantInfo { Name = participantName, Grade = participantGrade });
        }

        // Update both Participants and Grades fields
        course.Participants = JsonSerializer.Serialize(participantsAndGrades);
        course.Grades = JsonSerializer.Serialize(participantsAndGrades);

        // Save changes
        await _context.SaveChangesAsync();

        return (true, $"Successfully assigned grade {participantGrade} to {participantName} in {courseName}!");
    }
}