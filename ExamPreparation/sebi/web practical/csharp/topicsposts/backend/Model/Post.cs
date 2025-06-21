public class Post
{
    public int Id { get; set; }
    public string User { get; set; } = "";
    public int TopicId { get; set; }
    public string Text { get; set; } = "";
    public DateTime Date { get; set; } = DateTime.Now;
}