package com.example.lab9.DTO;

import java.time.LocalDate;

public record ReserveDTO(

        Integer hotelRoomId, LocalDate start, LocalDate end) {

}
