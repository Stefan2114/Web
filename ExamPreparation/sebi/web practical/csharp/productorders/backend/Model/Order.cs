public class Order
{
    public int Id { get; set; }
    public string User { get; set; } = "";
    public int ProductId { get; set; }
    public int Quantity { get; set; }
}