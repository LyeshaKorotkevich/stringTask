package ru.clevertec.util.testData;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import ru.clevertec.model.Player;

import java.time.LocalDate;
import java.util.UUID;
import java.util.stream.Stream;

@Builder(setterPrefix = "with")
public class PlayerTestData {

    @Builder.Default
    private UUID id = UUID.fromString("e2f53c51-0f2d-4d66-b3fc-e59624aa8cf6");

    @Builder.Default
    private String name = "Aliaksey";

    @Builder.Default
    private String surname = "Korotkevich";

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Builder.Default
    private LocalDate dateBirth = LocalDate.of(2023, 11, 18);

    @Builder.Default
    private int number = 10;

    public Player buildPlayer() {
        return Player.builder()
                .id(id)
                .name(name)
                .surname(surname)
                .dateBirth(dateBirth)
                .number(number)
                .build();

    }

    public static Stream<Player> providePlayers() {
        return Stream.of(
                PlayerTestData.builder().build().buildPlayer(),
                PlayerTestData.builder()
                        .withId(UUID.fromString("3a5e6b18-02b6-4a52-9f14-07e21e0b8d31"))
                        .withName("Lionel")
                        .withSurname("Messi")
                        .withDateBirth(LocalDate.of(1987, 6, 24))
                        .build().buildPlayer(),
                PlayerTestData.builder()
                        .withId(UUID.fromString("4a0e67d3-48b0-4a36-b741-9f2ac75821d3"))
                        .withName("Cristiano")
                        .withSurname("Ronaldo")
                        .withDateBirth(LocalDate.of(1985, 2, 5))
                        .withNumber(7)
                        .build().buildPlayer(),
                PlayerTestData.builder()
                        .withId(UUID.fromString("a9c5d3e7-58b2-41a8-8fe9-b469fca62f14"))
                        .withName("Neymar")
                        .withSurname("Junior")
                        .withDateBirth(LocalDate.of(1992, 2, 5))
                        .withNumber(11)
                        .build().buildPlayer()
        );
    }
}
