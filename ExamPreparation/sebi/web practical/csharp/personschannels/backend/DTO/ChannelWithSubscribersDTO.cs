public class ChannelWithSubscribersDTO
{
    public int Id { get; set; }
    public string Name { get; set; }
    public string Description { get; set; }
    public List<SubscriberInfo> Subscribers { get; set; }
}