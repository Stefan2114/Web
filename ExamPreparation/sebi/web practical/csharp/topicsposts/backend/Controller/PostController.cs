using Microsoft.AspNetCore.Mvc;

[Route("api/[controller]")]
[ApiController]
public class PostsController : ControllerBase
{
    private readonly PostService _service;

    public PostsController(PostService service)
    {
        _service = service;
    }

    [HttpGet]
    public async Task<IActionResult> Get()
    {
        var posts = await _service.GetPostsAsync();
        return Ok(posts);
    }

    [HttpPost("add")]
    public async Task<IActionResult> Add([FromBody] PostAddDTO request)
    {
        await _service.AddPostAsync(request.User, request.TopicName, request.Text, DateTime.Now);
        return Ok(new { message = "Successfully added post!" });
    }

    [HttpPatch("modify")]
    public async Task<IActionResult> Modify([FromBody] PostModifyDTO request)
    {
        await _service.ModifyPostAsync(request.Id, request.Text, request.User);
        return Ok(new { message = "Successfully modified post!" });
    }
}