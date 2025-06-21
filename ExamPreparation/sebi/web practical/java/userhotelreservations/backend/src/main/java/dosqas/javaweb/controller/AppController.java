package dosqas.javaweb.controller;

import dosqas.javaweb.model.HotelRoom;
import dosqas.javaweb.model.Reservation;
import dosqas.javaweb.service.AppService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AppController {
    private final AppService appService;

    // used for dependency injection at runtime by spring
    public AppController(AppService appService) {
        this.appService = appService;
    }

    @PostMapping("/authorize")
    public Boolean checkUser(@RequestParam String userName, @RequestParam Integer password) {
        return appService.authenticateUser(userName, password);
    }

    @GetMapping("/getRooms")
    public List<HotelRoom> getRooms(@RequestParam LocalDate start, @RequestParam LocalDate end) {
        return appService.getHotelRoomsForPeriod(start, end);
    }

    @PostMapping("/reserve")
    public Boolean reserveRoom(@RequestParam String userName, @RequestParam Integer hotelRoomId,
                               @RequestParam LocalDate checkIn, @RequestParam LocalDate checkOut) {
        return appService.reserveHotelRoom(userName, hotelRoomId, checkIn, checkOut);
    }

    @GetMapping("/guestCount")
    public Integer getGuestCount(@RequestParam LocalDate date) {
        return appService.getTotalNumberOfGuestsForSpecificDate(date);
    }

    @GetMapping("/reservations")
    public List<Reservation> getReservations(@RequestParam String userName) {
        return appService.getReservationsForUser(userName);
    }
}