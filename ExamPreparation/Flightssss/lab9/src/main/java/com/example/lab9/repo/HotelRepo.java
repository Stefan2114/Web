package com.example.lab9.repo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.lab9.model.Flight;
import com.example.lab9.model.Hotel;

@Repository
public interface HotelRepo extends JpaRepository<Hotel, Integer> {
    @Query("SELECT h FROM Hotel h WHERE h.date = :date AND h.city = :city AND h.availableRooms > 0")
    List<Hotel> findAvailableHotels(@Param("date") LocalDate date, @Param("city") String city);

}
