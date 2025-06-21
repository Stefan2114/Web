package com.example.lab9.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.lab9.DTO.LoginRequest;
import com.example.lab9.DTO.ReserveDTO;
import com.example.lab9.model.HotelRoom;
import com.example.lab9.model.Reservation;
import com.example.lab9.service.AppService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api")

public class AppController {

    private final AppService appService;

    public AppController(AppService appService) {
        this.appService = appService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> checkUser(@RequestBody LoginRequest request,
            HttpServletRequest httpRequest) {
        System.out.println("I got here");
        if (appService.authenticateUser(request.username(), request.password())) {

            HttpSession session = httpRequest.getSession(true);
            session.setAttribute("SPRING_SECURITY_CONTEXT", request.username());
            session.setMaxInactiveInterval(30 * 60);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/rooms")
    public ResponseEntity<List<HotelRoom>> getRooms(@RequestParam LocalDate start, @RequestParam LocalDate end) {

        return ResponseEntity.ok(appService.getHotelRoomsForPeriod(start, end));
    }

    @PostMapping("/reserve")
    public ResponseEntity<Void> reserveRoom(@RequestBody ReserveDTO reserveDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // this is usually the username
        System.out.println(username);
        appService.reserveHotelRoom(username, reserveDTO.hotelRoomId(), reserveDTO.start(), reserveDTO.end());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("guest-count")
    public ResponseEntity<Integer> getGuestCount(@RequestParam LocalDate date) {
        return ResponseEntity.ok(appService.getTotalNrOfGuestsForSpecificDate(date));
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<Reservation>> getReservations() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // this is usually the username
        System.out.println(username);
        return ResponseEntity.ok(appService.getReservationsForUser(username));
    }

}
