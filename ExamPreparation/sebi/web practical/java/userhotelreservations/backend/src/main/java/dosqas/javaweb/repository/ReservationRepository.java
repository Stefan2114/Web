package dosqas.javaweb.repository;

import dosqas.javaweb.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    long countByCheckInDateLessThanEqualAndCheckOutDateGreaterThanEqual(LocalDate checkIn, LocalDate checkOut);
    List<Reservation> findByCheckInDateLessThanEqualAndCheckOutDateGreaterThanEqual(LocalDate checkIn, LocalDate checkOut);
    List<Reservation> findByUserId(Integer userId);
}