package com.example.lab9.repo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.lab9.model.Reservation;

@Repository
public interface ReservationRepo extends JpaRepository<Reservation, Integer> {

    @Query("SELECT r FROM Reservation r WHERE r.checkInDate >= :checkIn AND r.checkOutDate <= :checkOut")
    List<Reservation> getRoomsWithCheckInAndCheckout(@Param("checkIn") LocalDate checkIn,
            @Param("checkOut") LocalDate checkOut);

    List<Reservation> findByUserId(Integer id);

}