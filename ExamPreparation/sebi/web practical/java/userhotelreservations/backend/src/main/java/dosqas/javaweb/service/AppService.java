package dosqas.javaweb.service;

import dosqas.javaweb.model.HotelRoom;
import dosqas.javaweb.model.Reservation;
import dosqas.javaweb.model.User;
import dosqas.javaweb.repository.HotelRoomRepository;
import dosqas.javaweb.repository.ReservationRepository;
import dosqas.javaweb.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AppService {
    private final UserRepository userRepository;
    private final HotelRoomRepository hotelRoomRepository;
    private final ReservationRepository reservationRepository;

    // runtime injection
    public AppService(UserRepository userRepository,
                      HotelRoomRepository hotelRoomRepository,
                      ReservationRepository reservationRepository) {
        this.userRepository = userRepository;
        this.hotelRoomRepository = hotelRoomRepository;
        this.reservationRepository = reservationRepository;
    }

    private Integer getRoomPrice(LocalDate start, LocalDate end, Integer basePrice) {
        long hotelRoomCount = hotelRoomRepository.count();
        long numberOfReservations = reservationRepository.countByCheckInDateLessThanEqualAndCheckOutDateGreaterThanEqual(start, end);

        long bookedPercent = hotelRoomCount * numberOfReservations / 100;

        if (bookedPercent <= 50) {
            return basePrice;
        }
        else if (bookedPercent <= 80) {
            return basePrice + basePrice * 2 / 10;
        }
        else return basePrice + basePrice / 2;
    }

    public Boolean authenticateUser(String userName, Integer password) {
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return password.equals(user.getPassword());
    }

    public List<HotelRoom> getHotelRoomsForPeriod(LocalDate start, LocalDate end) {
        // Find IDs of rooms with overlapping reservations
        List<Reservation> overlappingReservations = reservationRepository
                .findByCheckInDateLessThanEqualAndCheckOutDateGreaterThanEqual(end, start);

        Set<Integer> reservedRoomIds = overlappingReservations.stream()
                .map(res -> res.getHotelRoom().getId())
                .collect(Collectors.toSet());

        // Return only rooms not in the reserved set
        return hotelRoomRepository.findAll().stream()
                .filter(room -> !reservedRoomIds.contains(room.getId()))
                .collect(Collectors.toList());
    }

    public Boolean reserveHotelRoom(String userName, Integer hotelRoomId,
                                    LocalDate checkIn, LocalDate checkOut) {
        if (checkIn.isAfter(checkOut)) {
            throw new RuntimeException("Check out date is after check out date");
        }

        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Integer userId = user.getId();

        List<Reservation> reservations = reservationRepository.findByUserId(userId);
        for (Reservation reservation : reservations) {
            if (reservation.getCheckInDate().isAfter(checkOut) || reservation.getCheckOutDate().isBefore(checkIn)) {
                continue;
            }

            throw new RuntimeException("Reservations overlap");
        }

        Reservation reservation = new Reservation();
        reservation.setCheckInDate(checkIn);
        reservation.setCheckOutDate(checkOut);

        HotelRoom hotelRoom = hotelRoomRepository.findById(hotelRoomId)
                        .orElseThrow(() -> new RuntimeException("HotelRoom not found"));
        reservation.setHotelRoom(hotelRoom);

        reservation.setNumberOfGuests(hotelRoom.getCapacity());
        reservation.setUserId(userId);

        reservation.setTotalPrice(getRoomPrice(checkIn, checkOut, hotelRoom.getBasePrice()));

        reservationRepository.save(reservation);

        return true;
    }

    public Integer getTotalNumberOfGuestsForSpecificDate(LocalDate date) {
        List<Reservation> reservations = reservationRepository.findByCheckInDateLessThanEqualAndCheckOutDateGreaterThanEqual(date, date);
        Integer numberOfGuests = 0;

        for (Reservation reservation : reservations) {
            numberOfGuests += reservation.getNumberOfGuests();
        }

        return numberOfGuests;
    }

    public List<Reservation> getReservationsForUser(String userName) {
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return reservationRepository.findByUserId(user.getId());
    }
}