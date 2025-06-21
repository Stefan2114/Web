using System.Collections.Concurrent;
using System.Net.WebSockets;
using System.Text;
using System.Text.Json;
using Microsoft.Extensions.Logging;

public class WebSocketHandler
{
    private static readonly ConcurrentDictionary<string, WebSocket> _sockets = new();
    private static readonly JsonSerializerOptions _jsonOptions = new() { PropertyNameCaseInsensitive = true };
    private static ILogger<WebSocketHandler>? _logger;

    public static void Initialize(ILogger<WebSocketHandler> logger)
    {
        _logger = logger;
    }

    public static async Task HandleWebSocketConnection(WebSocket webSocket, string username)
    {
        _logger?.LogInformation("New WebSocket connection for user: {Username}", username);
        _sockets.TryAdd(username, webSocket);

        try
        {
            var buffer = new byte[1024 * 4];
            while (webSocket.State == WebSocketState.Open)
            {
                var result = await webSocket.ReceiveAsync(new ArraySegment<byte>(buffer), CancellationToken.None);
                if (result.MessageType == WebSocketMessageType.Close)
                {
                    _logger?.LogInformation("WebSocket close message received for user: {Username}", username);
                    await HandleDisconnection(username);
                    break;
                }
            }
        }
        catch (Exception ex)
        {
            _logger?.LogError(ex, "Error handling WebSocket connection for user: {Username}", username);
            await HandleDisconnection(username);
        }
    }

    public static async Task BroadcastNewPost(string postJson)
    {
        _logger?.LogInformation("Broadcasting new post to {Count} connected clients", _sockets.Count);
        
        var tasks = _sockets.Select(async socket =>
        {
            try
            {
                if (socket.Value.State == WebSocketState.Open)
                {
                    var bytes = Encoding.UTF8.GetBytes(postJson);
                    await socket.Value.SendAsync(new ArraySegment<byte>(bytes), WebSocketMessageType.Text, true, CancellationToken.None);
                    _logger?.LogDebug("Post broadcasted to user: {Username}", socket.Key);
                }
                else
                {
                    _logger?.LogWarning("Skipping broadcast to user {Username} - WebSocket not open", socket.Key);
                }
            }
            catch (Exception ex)
            {
                _logger?.LogError(ex, "Error broadcasting to user: {Username}", socket.Key);
            }
        });

        await Task.WhenAll(tasks);
    }

    private static async Task HandleDisconnection(string username)
    {
        _logger?.LogInformation("Handling disconnection for user: {Username}", username);
        if (_sockets.TryRemove(username, out var socket))
        {
            try
            {
                if (socket.State == WebSocketState.Open)
                {
                    await socket.CloseAsync(WebSocketCloseStatus.NormalClosure, "Connection closed", CancellationToken.None);
                }
            }
            catch (Exception ex)
            {
                _logger?.LogError(ex, "Error closing WebSocket for user: {Username}", username);
            }
        }
    }
} 