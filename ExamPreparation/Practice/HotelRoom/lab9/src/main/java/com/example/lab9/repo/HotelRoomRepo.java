package com.example.lab9.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.lab9.model.HotelRoom;

@Repository
public interface HotelRoomRepo extends JpaRepository<HotelRoom, Integer> {

}
