package com.example.lab9.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.lab9.model.Reservation;

@Repository
public interface ReservationRepo extends JpaRepository<Reservation, Integer> {

}