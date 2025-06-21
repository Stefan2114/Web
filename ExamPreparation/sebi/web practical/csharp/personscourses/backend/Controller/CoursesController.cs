using Microsoft.AspNetCore.Mvc;

[Route("api/[controller]")]
[ApiController]
public class CoursesController : ControllerBase
{
    private readonly CourseService _service;

    public CoursesController(CourseService service)
    {
        _service = service;
    }

    [HttpGet]
    public async Task<IActionResult> Owned()
    {
        var courses = await _service.GetCoursesAsync();
        return Ok(courses);
    }

    [HttpGet("participant")]
    public async Task<IActionResult> Subscribed(string participantName)
    {
        var courses = await _service.GetCoursesForParticipantAsync(participantName);
        return Ok(courses);
    }

    [HttpPost("assign")]
    public async Task<IActionResult> Subscribe([FromBody] GradeAssignRequest request)
    {
        var (success, message) = await _service.AssignGradeToParticipantAsync(
            request.CourseName!, 
            request.ProfessorName!, 
            request.ParticipantName, 
            request.ParticipantGrade);

        if (success)
        {
            return Ok(new { success = true, message = message });
        }
        else
        {
            return BadRequest(new { success = false, message = message });
        }
    }
}