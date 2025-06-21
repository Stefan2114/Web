public class Reservation
{
    public int Id { get; set; }
    public string Person { get; set; } = "";
    public string Type { get; set; } = "";
    public int IdReservedResource { get; set; }
}