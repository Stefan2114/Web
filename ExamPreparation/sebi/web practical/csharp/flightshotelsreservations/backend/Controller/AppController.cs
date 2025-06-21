using Microsoft.AspNetCore.Mvc;

[Route("api")]
[ApiController]
public class AppController : ControllerBase
{
    private readonly AppService _service;

    public AppController(AppService service)
    {
        _service = service;
    }

    [HttpGet("flights")]
    public async Task<IActionResult> GetFlights(DateOnly date, string city)
    {
        var flights = await _service.GetFlightsAsync(date, city);
        return Ok(flights);
    }

    [HttpGet("hotels")]
    public async Task<IActionResult> GetHotels(DateOnly date, string city)
    {
        var hotels = await _service.GetHotelsAsync(date, city);
        return Ok(hotels);
    }

    [HttpPost("reserveFlight")]
    public async Task<IActionResult> ReserveFlight([FromBody] ReservationRequest request)
    {
        var (success, message, reservationId) = await _service.ReserveSeatOnFlight(
            request.Username!,
            request.ReservedResourceId!);

        if (success)
        {
            return Ok(new { success = true, message = message, reservationId = reservationId });
        }
        else
        {
            return BadRequest(new { success = false, message = message });
        }
    }

    [HttpPost("reserveHotel")]
    public async Task<IActionResult> ReserveHotel([FromBody] ReservationRequest request)
    {
        var (success, message, reservationId) = await _service.ReserveHotelRoom(
            request.Username!,
            request.ReservedResourceId);

        if (success)
        {
            return Ok(new { success = true, message = message, reservationId = reservationId });
        }
        else
        {
            return BadRequest(new { success = false, message = message });
        }
    }

    [HttpDelete("cancelReservations")]
    public async Task<IActionResult> CancelReservations(string username, string reservationIdsString)
    {
        var (success, message) = await _service.CancelReservations(
            username,
            reservationIdsString);

        if (success)
        {
            return Ok(new { success = true, message = message });
        }
        else
        {
            return BadRequest(new { success = false, message = message });
        }
    }
}