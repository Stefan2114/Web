package com.example.lab9.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.lab9.model.Task;

public interface TaskRepository extends JpaRepository<Task, Integer> {

    // @Query("SELECT r FROM Reservation r WHERE r.checkInDate >= :checkIn AND
    // r.checkOutDate <= :checkOut")
    // List<Reservation> getRoomsWithCheckInAndCheckout(@Param("checkIn") LocalDate
    // checkIn,
    // @Param("checkOut") LocalDate checkOut);

}
