using System.Net.WebSockets;
using Microsoft.AspNetCore.Mvc;

[ApiController]
[Route("api/[controller]")]
public class WebSocketController : ControllerBase
{
    private readonly ILogger<WebSocketController> _logger;

    public WebSocketController(ILogger<WebSocketController> logger)
    {
        _logger = logger;
    }

    [Route("ws")]
    public async Task Get()
    {
        if (HttpContext.WebSockets.IsWebSocketRequest)
        {
            var username = HttpContext.Request.Query["username"].ToString();
            if (string.IsNullOrEmpty(username))
            {
                _logger.LogWarning("WebSocket connection attempt without username");
                HttpContext.Response.StatusCode = 400;
                return;
            }

            _logger.LogInformation("WebSocket connection request from user: {Username}", username);
            using var webSocket = await HttpContext.WebSockets.AcceptWebSocketAsync();
            await WebSocketHandler.HandleWebSocketConnection(webSocket, username);
        }
        else
        {
            _logger.LogWarning("Non-WebSocket request to WebSocket endpoint");
            HttpContext.Response.StatusCode = 400;
        }
    }
} 