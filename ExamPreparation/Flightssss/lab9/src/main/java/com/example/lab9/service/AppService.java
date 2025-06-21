package com.example.lab9.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.lab9.model.Flight;
import com.example.lab9.model.Hotel;
import com.example.lab9.model.Reservation;
import com.example.lab9.repo.FlightRepo;
import com.example.lab9.repo.HotelRepo;
import com.example.lab9.repo.ReservationRepo;

@Service
@Transactional
public class AppService {

    private final FlightRepo flightRepo;
    private final HotelRepo hotelRepo;
    private final ReservationRepo reservationRepo;

    public AppService(FlightRepo flightRepo, HotelRepo hotelRepo, ReservationRepo reservationRepo) {
        this.flightRepo = flightRepo;
        this.hotelRepo = hotelRepo;
        this.reservationRepo = reservationRepo;

    }

    public List<Flight> getAvailableFlights(LocalDate date, String city) {

        return this.flightRepo.findAvailableFlights(date, city);
    }

    public void addReservation(Reservation reservation) {
        this.reservationRepo.save(reservation);
        if (reservation.getType().equals("flight")) {
            Flight flight = flightRepo.findById(reservation.getIdReservedResource()).get();
            flight.setAvailableSeats(flight.getAvailableSeats() - 1);
            flightRepo.save(flight);
        } else {
            Hotel hotel = hotelRepo.findById(reservation.getIdReservedResource()).get();
            hotel.setAvailableRooms(hotel.getAvailableRooms() - 1);
            hotelRepo.save(hotel);
        }
    }

    public List<Hotel> getAvailableHotels(LocalDate date, String city) {
        // TODO Auto-generated method stub
        return this.hotelRepo.findAvailableHotels(date, city);
    }

    // private Integer getRoomPrice(LocalDate start, LocalDate end, Integer
    // basePrice) {

    // long hotelRoomCount = hotelRoomRepo.count();
    // long numberOfReservations =
    // reservationRepo.getRoomsWithCheckInAndCheckout(start, end).size();

    // long bookedPercent = hotelRoomCount * numberOfReservations / 100;

    // if (bookedPercent <= 50) {
    // return basePrice;
    // }

    // if (basePrice <= 80) {
    // return basePrice + basePrice * 2 / 10;
    // }

    // return basePrice + basePrice / 2;
    // }

    // public Boolean authenticateUser(String username, String password) {
    // User user = this.userRepo.findByUsername(username).orElseThrow(() -> new
    // RuntimeException("user not found"));

    // return password.equals(user.getPassword());
    // }

    // public List<HotelRoom> getHotelRoomsForPeriod(LocalDate start, LocalDate end)
    // {
    // List<Reservation> reservations =
    // reservationRepo.getRoomsWithCheckInAndCheckout(start, end);

    // Set<Integer> reservedRoomIds = reservations.stream().map(res ->
    // res.getHotelRoom().getId())
    // .collect(Collectors.toSet());

    // return hotelRoomRepo.findAll().stream().filter(room ->
    // !reservedRoomIds.contains(room.getId())).toList();

    // }

    // public void reserveHotelRoom(String userName, Integer hotelRoomId, LocalDate
    // start, LocalDate end) {
    // if (start.isAfter(end)) {
    // throw new RuntimeException("Invalid dates");
    // }

    // User user = userRepo.findByUsername(userName).get();

    // List<Reservation> reservations = reservationRepo.findByUserId(hotelRoomId);

    // for (Reservation reservation : reservations) {
    // if (reservation.getCheckInDate().isAfter(end) ||
    // reservation.getCheckOutDate().isBefore(start)) {
    // continue;
    // }

    // throw new RuntimeException("Reservations overlaps");
    // }

    // Reservation reservation = new Reservation();
    // reservation.setCheckInDate(start);
    // reservation.setCheckOutDate(end);

    // HotelRoom hotelRoom = hotelRoomRepo.findById(hotelRoomId).get();
    // reservation.setHotelRoom(hotelRoom);
    // reservation.setUser(user);

    // reservation.setTotalPrice(getRoomPrice(start, end,
    // hotelRoom.getBasePrice()));

    // reservationRepo.save(reservation);

    // }

    // public int getTotalNrOfGuestsForSpecificDate(LocalDate date) {

    // List<Reservation> reservations =
    // reservationRepo.getRoomsWithCheckInAndCheckout(date, date);
    // Integer numberOfGuests = 0;

    // for (Reservation reservation : reservations) {
    // numberOfGuests += reservation.getNumberOfGuests();
    // }

    // return numberOfGuests;
    // }

    // public List<Reservation> getReservationsForUser(String username) {
    // User user = userRepo.findByUsername(username).get();
    // return reservationRepo.findByUserId(user.getId());
    // }

}
