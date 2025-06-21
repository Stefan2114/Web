package com.example.lab9.DTO;

import com.example.lab9.Enum.StatusType;

public record UpdatedTaskDTO(

        Integer id,
        StatusType newStatus,
        String username) {

}
