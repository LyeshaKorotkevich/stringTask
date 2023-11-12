package ru.clevertec.util.testData;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import ru.clevertec.model.ChampionsLeague;
import ru.clevertec.model.Team;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Builder(setterPrefix = "with")
public class ChampionsLeagueTestData {

    @Builder.Default
    private UUID id = UUID.fromString("1a0d65d2-1e0a-4e12-9956-1d98c8c24539");

    @Builder.Default
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private OffsetDateTime dateStart = OffsetDateTime.parse("2023-11-07T12:34:56Z");

    @Builder.Default
    private Map<Character, List<Team>> groups = Map.ofEntries(
            Map.entry('A', TeamTestData.provideTeams().toList()),
            Map.entry('B', TeamTestData.provideTeams().toList())
            );

    public ChampionsLeague buildChampionsLeague() {
        return ChampionsLeague.builder()
                .id(id)
                .dateStart(dateStart)
                .groups(groups)
                .build();
    }
}
