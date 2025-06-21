public class Flight
{
    public int FlightID { get; set; }
    public DateOnly Date { get; set; } = DateOnly.FromDateTime(DateTime.Now);
    public string DestinationCity { get; set; } = "";
    public int AvailableSeats { get; set; }
}