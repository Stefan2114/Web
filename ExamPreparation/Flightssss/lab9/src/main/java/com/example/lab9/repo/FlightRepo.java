package com.example.lab9.repo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.lab9.model.Flight;

@Repository
public interface FlightRepo extends JpaRepository<Flight, Integer> {

    @Query("SELECT f FROM Flight f WHERE f.date = :date AND f.destinationCity = :city AND f.availableSeats > 0")
    List<Flight> findAvailableFlights(@Param("date") LocalDate date, @Param("city") String city);

}
