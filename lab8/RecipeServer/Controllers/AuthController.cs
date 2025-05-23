using Microsoft.AspNetCore.Mvc;

[ApiController]
[Route("api/[controller]")]
public class AuthController : ControllerBase
{
    private readonly AppDbContext _context;

    public AuthController(AppDbContext context) => _context = context;

    [HttpPost("login")]
    public IActionResult Login([FromBody] User user)
    {
        try
        {
            if (string.IsNullOrWhiteSpace(user.Username) || string.IsNullOrWhiteSpace(user.Password))
            {
                return BadRequest(new { error = "Username and password are required" });
            }

            var found = _context.Users.FirstOrDefault(u =>
                u.Username == user.Username && u.Password == user.Password);

            if (found != null)
            {
                HttpContext.Session.SetString("user", found.Username);
                return Ok(new { message = "Logged in", username = found.Username });
            }

            return Unauthorized(new { error = "Invalid credentials" });
        }
        catch (Exception ex)
        {
            return StatusCode(500, new { error = "Internal server error", details = ex.Message });
        }
    }

    [HttpPost("logout")]
    public IActionResult Logout()
    {
        try
        {
            HttpContext.Session.Clear();
            return Ok(new { message = "Logged out" });
        }
        catch (Exception ex)
        {
            return StatusCode(500, new { error = "Internal server error", details = ex.Message });
        }
    }

    [HttpGet("status")]
    public IActionResult GetStatus()
    {
        var user = HttpContext.Session.GetString("user");
        if (!string.IsNullOrEmpty(user))
        {
            return Ok(new { loggedIn = true, username = user });
        }
        return Ok(new { loggedIn = false });
    }
}