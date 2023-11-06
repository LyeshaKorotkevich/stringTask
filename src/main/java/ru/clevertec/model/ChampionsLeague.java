package ru.clevertec.model;

import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
public class ChampionsLeague {
    private UUID id;
    private OffsetDateTime dateStart;
    private Map<Character, List<Team>> groups;
}
