using Microsoft.EntityFrameworkCore;
using System.Text.Json;

public class PostService
{
    private readonly AppDbContext _context;
    private static readonly JsonSerializerOptions _jsonOptions = new() { PropertyNameCaseInsensitive = true };

    public PostService(AppDbContext context)
    {
        _context = context;
    }

    public async Task<List<PostDataDTO>> GetPostsAsync()
    {
        var posts = await _context.Posts.ToListAsync();

        var result = new List<PostDataDTO>();
        foreach (var p in posts)
        {
            var topic = await _context.Topics.FirstOrDefaultAsync(t => t.Id == p.TopicId);
            result.Add(new PostDataDTO
            {
                Id = p.Id,
                User = p.User,
                TopicName = topic!.TopicName,
                Text = p.Text,
                Date = p.Date
            });
        }

        return result;
    }

    public async Task ModifyPostAsync(int id, string text, string username)
    {
        var post = await _context.Posts.FirstOrDefaultAsync(p => p.Id == id);
        if (post == null) return;

        post.Text = text;
        post.User = username;
        post.Date = DateTime.Now;

        await _context.SaveChangesAsync();

        var topic = await _context.Topics.FirstOrDefaultAsync(t => t.Id == post.TopicId);
        var notificationPost = new PostDataDTO
        {
            Id = post.Id,
            User = post.User,
            TopicName = topic!.TopicName,
            Text = post.Text,
            Date = post.Date
        };

        var postJson = JsonSerializer.Serialize(notificationPost, _jsonOptions);
        await WebSocketHandler.BroadcastNewPost(postJson);
    }

    public async Task AddPostAsync(string username, string topicName, string text, DateTime date)
    {
        var topic = await _context.Topics.FirstOrDefaultAsync(t => t.TopicName == topicName);

        if (topic == null)
        {
            topic = new Topic { TopicName = topicName };
            _context.Topics.Add(topic);
            await _context.SaveChangesAsync();
        }

        var post = new Post { User = username, TopicId = topic.Id, Text = text, Date = date };
        _context.Posts.Add(post);

        await _context.SaveChangesAsync();

        var notificationPost = new PostDataDTO
        {
            Id = post.Id,
            User = post.User,
            TopicName = topic.TopicName,
            Text = post.Text,
            Date = post.Date
        };

        var postJson = JsonSerializer.Serialize(notificationPost, _jsonOptions);
        await WebSocketHandler.BroadcastNewPost(postJson);
    }
}