public class Hotel
{
    public int HotelID { get; set; }
    public string HotelName { get; set; } = "";
    public string City { get; set; } = "";
    public DateOnly Date { get; set; } = DateOnly.FromDateTime(DateTime.Now);
    public int AvailableRooms { get; set; }
}