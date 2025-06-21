using System.Text.Json;
using Microsoft.EntityFrameworkCore;

public class ChannelService
{
    private readonly AppDbContext _context;

    public ChannelService(AppDbContext context)
    {
        _context = context;
    }

    public async Task<List<ChannelWithSubscribersDTO>> GetChannelsByOwnerNameAsync(string ownerName)
    {
        var owner = await _context.Persons.FirstOrDefaultAsync(p => p.Name == ownerName);
        if (owner == null) return new List<ChannelWithSubscribersDTO>();

        var channels = await _context.Channels
            .Where(c => c.OwnerId == owner.Id)
            .ToListAsync();

        var result = channels.Select(c => new ChannelWithSubscribersDTO
        {
            Id = c.Id,
            Name = c.Name,
            Description = c.Description,
            Subscribers = string.IsNullOrEmpty(c.Subscribers)
                ? new List<SubscriberInfo>()
                : JsonSerializer.Deserialize<List<SubscriberInfo>>(c.Subscribers)
        }).ToList();

        return result;
    }

    public async Task<List<SubscribedChannelDTO>> GetChannelsSubscribedByUserAsync(string userName)
    {
        var channels = await _context.Channels.ToListAsync();
        return channels
            .Where(c =>
            {
                var subs = string.IsNullOrEmpty(c.Subscribers)
                    ? new List<SubscriberInfo>()
                    : JsonSerializer.Deserialize<List<SubscriberInfo>>(c.Subscribers);
                return subs.Any(s => s.Name == userName);
            })
            .Select(c => new SubscribedChannelDTO { Name = c.Name, Description = c.Description, Id = c.Id })
            .ToList();
    }

    public async Task SubscribeUserAsync(int channelId, string userName)
    {
        var channel = await _context.Channels.FindAsync(channelId);
        if (channel == null) return;

        var subs = string.IsNullOrEmpty(channel.Subscribers)
            ? new List<SubscriberInfo>()
            : JsonSerializer.Deserialize<List<SubscriberInfo>>(channel.Subscribers);

        var existing = subs.FirstOrDefault(s => s.Name == userName);
        if (existing != null)
        {
            existing.Date = DateTime.Now;
        }
        else
        {
            subs.Add(new SubscriberInfo { Name = userName, Date = DateTime.Now });
        }

        channel.Subscribers = JsonSerializer.Serialize(subs);
        await _context.SaveChangesAsync();
    }
}