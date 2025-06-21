using Microsoft.AspNetCore.Mvc;

[Route("api/[controller]")]
[ApiController]
public class ChannelsController : ControllerBase
{
    private readonly ChannelService _service;

    public ChannelsController(ChannelService service)
    {
        _service = service;
    }

    [HttpGet("owned")]
    public async Task<IActionResult> Owned(string ownerName)
    {
        var channels = await _service.GetChannelsByOwnerNameAsync(ownerName);
        return Ok(channels);
    }

    [HttpGet("subscribed")]
    public async Task<IActionResult> Subscribed(string userName)
    {
        var channels = await _service.GetChannelsSubscribedByUserAsync(userName);
        return Ok(channels);
    }

    [HttpPost("subscribe")]
    public async Task<IActionResult> Subscribe([FromBody] SubscribeRequestDto request)
    {
        await _service.SubscribeUserAsync(request.ChannelId, request.UserName);
        return Ok(new { message = "Successfully subscribed to channel!" });
    }
}