package com.example.lab9.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.lab9.model.HotelRoom;
import com.example.lab9.model.Reservation;
import com.example.lab9.model.User;
import com.example.lab9.repo.HotelRoomRepo;
import com.example.lab9.repo.ReservationRepo;
import com.example.lab9.repo.UserRepo;

@Service
@Transactional
public class AppService {

    private final UserRepo userRepo;
    private final HotelRoomRepo hotelRoomRepo;
    private final ReservationRepo reservationRepo;

    public AppService(UserRepo userRepo, HotelRoomRepo hotelRoomRepo, ReservationRepo reservationRepo) {
        this.userRepo = userRepo;
        this.hotelRoomRepo = hotelRoomRepo;
        this.reservationRepo = reservationRepo;

    }

    private Integer getRoomPrice(LocalDate start, LocalDate end, Integer basePrice) {

        long hotelRoomCount = hotelRoomRepo.count();
        long numberOfReservations = reservationRepo.getRoomsWithCheckInAndCheckout(start, end).size();

        long bookedPercent = hotelRoomCount * numberOfReservations / 100;

        if (bookedPercent <= 50) {
            return basePrice;
        }

        if (basePrice <= 80) {
            return basePrice + basePrice * 2 / 10;
        }

        return basePrice + basePrice / 2;
    }

    public Boolean authenticateUser(String username, String password) {
        User user = this.userRepo.findByUsername(username).orElseThrow(() -> new RuntimeException("user not found"));

        return password.equals(user.getPassword());
    }

    public List<HotelRoom> getHotelRoomsForPeriod(LocalDate start, LocalDate end) {
        List<Reservation> reservations = reservationRepo.getRoomsWithCheckInAndCheckout(start, end);

        Set<Integer> reservedRoomIds = reservations.stream().map(res -> res.getHotelRoom().getId())
                .collect(Collectors.toSet());

        return hotelRoomRepo.findAll().stream().filter(room -> !reservedRoomIds.contains(room.getId())).toList();

    }

    public void reserveHotelRoom(String userName, Integer hotelRoomId, LocalDate start, LocalDate end) {
        if (start.isAfter(end)) {
            throw new RuntimeException("Invalid dates");
        }

        User user = userRepo.findByUsername(userName).get();

        List<Reservation> reservations = reservationRepo.findByUserId(hotelRoomId);

        for (Reservation reservation : reservations) {
            if (reservation.getCheckInDate().isAfter(end) || reservation.getCheckOutDate().isBefore(start)) {
                continue;
            }

            throw new RuntimeException("Reservations overlaps");
        }

        Reservation reservation = new Reservation();
        reservation.setCheckInDate(start);
        reservation.setCheckOutDate(end);

        HotelRoom hotelRoom = hotelRoomRepo.findById(hotelRoomId).get();
        reservation.setHotelRoom(hotelRoom);
        reservation.setUser(user);

        reservation.setTotalPrice(getRoomPrice(start, end, hotelRoom.getBasePrice()));

        reservationRepo.save(reservation);

    }

    public int getTotalNrOfGuestsForSpecificDate(LocalDate date) {

        List<Reservation> reservations = reservationRepo.getRoomsWithCheckInAndCheckout(date, date);
        Integer numberOfGuests = 0;

        for (Reservation reservation : reservations) {
            numberOfGuests += reservation.getNumberOfGuests();
        }

        return numberOfGuests;
    }

    public List<Reservation> getReservationsForUser(String username) {
        User user = userRepo.findByUsername(username).get();
        return reservationRepo.findByUserId(user.getId());
    }

}
