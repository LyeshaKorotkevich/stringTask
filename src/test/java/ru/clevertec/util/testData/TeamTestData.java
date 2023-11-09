package ru.clevertec.util.testData;

import lombok.Builder;
import ru.clevertec.model.Player;
import ru.clevertec.model.Team;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Builder(setterPrefix = "with")
public class TeamTestData {

    @Builder.Default
    private UUID id = UUID.fromString("4f8cbf8b-7218-4dcd-8d8c-4f5c4a3b6dab");

    @Builder.Default
    private String name = "Barcelona";

    @Builder.Default
    private String country = "Spain";

    @Builder.Default
    private List<Player> players = PlayerTestData.providePlayers().toList();

    public Team buildTeam() {
        return Team.builder()
                .id(id)
                .name(name)
                .country(country)
                .players(players)
                .build();
    }

    public static Stream<Team> provideTeams() {
        return Stream.of(
                TeamTestData.builder().build().buildTeam(),
                TeamTestData.builder()
                        .withId(UUID.fromString("c9445bfc-2769-42a8-ae2f-136d399ae0b0"))
                        .withName("Real Madrid")
                        .withCountry("Spain")
                        .withPlayers(Arrays.asList(
                                PlayerTestData.builder()
                                        .withId(UUID.fromString("b7b7d20f-20db-47cb-87b2-dba65e42275c"))
                                        .withName("Karim")
                                        .withSurname("Benzema")
                                        .withDateBirth(LocalDate.of(1987, 12, 19))
                                        .withNumber(9)
                                        .build().buildPlayer(),
                                PlayerTestData.builder()
                                        .withId(UUID.fromString("f84998a2-c526-40c4-8ee9-f35b22866c98"))
                                        .withName("Sergio")
                                        .withSurname("Ramos")
                                        .withDateBirth(LocalDate.of(1986, 3, 30))
                                        .withNumber(4)
                                        .build().buildPlayer()
                        ))
                        .build().buildTeam(),
                TeamTestData.builder()
                        .withId(UUID.fromString("f3a34d56-187e-4cc7-8dfb-7e9ff433d8c9"))
                        .withName("Liverpool")
                        .withCountry("England")
                        .withPlayers(Arrays.asList(
                                PlayerTestData.builder()
                                        .withId(UUID.fromString("aeb7f3f9-20aa-4a67-b6ce-e8e2e0b16f75"))
                                        .withName("Mohamed")
                                        .withSurname("Salah")
                                        .withDateBirth(LocalDate.of(1992, 6, 15))
                                        .withNumber(11)
                                        .build().buildPlayer(),
                                PlayerTestData.builder()
                                        .withId(UUID.fromString("f6c8a25e-5e24-4c8f-b89f-4e8e81010023"))
                                        .withName("Sadio")
                                        .withSurname("Mane")
                                        .withDateBirth(LocalDate.of(1992, 4, 10))
                                        .withNumber(10)
                                        .build().buildPlayer()
                        ))
                        .build().buildTeam()
        );
    }

}
