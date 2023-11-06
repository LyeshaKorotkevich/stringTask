package ru.clevertec.model;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class Team {
    private UUID id;
    private String name;
    private String country;
    private List<Player> players;

}
