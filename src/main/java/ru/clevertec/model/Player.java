package ru.clevertec.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class Player {
    private UUID id;
    private String name;
    private String surname;
    private LocalDate dateBirth;
    private int number;

}
