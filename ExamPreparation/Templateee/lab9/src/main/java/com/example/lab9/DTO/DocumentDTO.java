package com.example.lab9.DTO;

import java.util.List;

public record DocumentDTO(

                Integer id,
                String name,
                String content,
                List<String> authors

) {

}
