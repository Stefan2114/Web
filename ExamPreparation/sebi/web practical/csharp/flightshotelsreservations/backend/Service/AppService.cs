using System.Text.Json;
using System.Text.Json.Serialization;
using Microsoft.EntityFrameworkCore;

public class AppService
{
    private readonly AppDbContext _context;

    public AppService(AppDbContext context)
    {
        _context = context;
    }

    public async Task<List<Flight>> GetFlightsAsync(DateOnly date, string destinationCity)
    {
        return await _context.Flights.Where(flight =>
            flight.Date == date
            && flight.DestinationCity == destinationCity
            && flight.AvailableSeats > 0)
            .ToListAsync();
    }

    public async Task<List<Hotel>> GetHotelsAsync(DateOnly date, string city)
    {
        return await _context.Hotels.Where(hotel =>
            hotel.Date == date
            && hotel.City == city
            && hotel.AvailableRooms > 0)
            .ToListAsync();
    }

    public async Task<(bool Success, string Message, int reservationId)> ReserveSeatOnFlight(string username, int flightId)
    {
        var reservation = new Reservation
        {
            Person = username,
            Type = "flight",
            IdReservedResource = flightId
        };

        var flight = await _context.Flights.FirstOrDefaultAsync(f => f.FlightID == flightId);
        flight!.AvailableSeats--;

        _context.Reservations.Add(reservation);

        await _context.SaveChangesAsync();

        return (true, "Successfully added flight reservation", reservation.Id);
    }

    public async Task<(bool Success, string Message, int reservationId)> ReserveHotelRoom(string username, int hotelId)
    {
        var reservation = new Reservation
        {
            Person = username,
            Type = "hotel",
            IdReservedResource = hotelId
        };

        var hotel = await _context.Hotels.FirstOrDefaultAsync(h => h.HotelID == hotelId);
        hotel!.AvailableRooms--;

        _context.Reservations.Add(reservation);

        await _context.SaveChangesAsync();

        return (true, "Successfully added hotel reservation", reservation.Id);
    }

    public async Task<(bool Success, string Message)> CancelReservations(string username, string reservationIdsString)
    {
        var reservationIds = reservationIdsString.Split(",").ToList();

        var reservations = await _context.Reservations.ToListAsync();

        foreach (var reservation in reservations.Where(r => reservationIds.Contains(r.Id.ToString())))
        {
            if (reservation.Type == "flight")
            {
                var flight = await _context.Flights.FirstOrDefaultAsync(f => f.FlightID == reservation.IdReservedResource);
                flight!.AvailableSeats++;
            }
            else
            {
                var hotel = await _context.Hotels.FirstOrDefaultAsync(h => h.HotelID == reservation.IdReservedResource);
                hotel!.AvailableRooms++;
            }
        }

        reservations.RemoveAll(r => reservationIds.Contains(r.Id.ToString()));

        await _context.SaveChangesAsync();

        return (true, "Successfully removed reservations");
    }
}