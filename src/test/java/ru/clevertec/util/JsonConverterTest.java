package ru.clevertec.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.clevertec.model.ChampionsLeague;
import ru.clevertec.model.Player;
import ru.clevertec.model.Team;
import ru.clevertec.util.testData.ChampionsLeagueTestData;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JsonConverterTest {

    private final ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();

    @Nested
    class ToJsonTest {

        @ParameterizedTest
        @MethodSource("ru.clevertec.util.testData.PlayerTestData#providePlayers")
        void toJsonShouldReturnStringFromPlayerObject(Player player) throws JsonProcessingException {
            // given
            String expected = mapper.writeValueAsString(player);

            // when
            String actual = JsonConverter.toJson(player);

            // then
            assertEquals(expected, actual);
        }

        @ParameterizedTest
        @MethodSource("ru.clevertec.util.testData.TeamTestData#provideTeams")
        void toJsonShouldReturnStringFromTeamObject(Team team) throws JsonProcessingException {
            // given
            String expected = mapper.writeValueAsString(team);

            // when
            String actual = JsonConverter.toJson(team);

            // then
            assertEquals(expected, actual);
        }

        @Test
        void toJsonShouldReturnStringFromChampionsLeagueObject() throws JsonProcessingException {
            // given
            ChampionsLeague championsLeague = ChampionsLeagueTestData.builder().build().buildChampionsLeague();
            String expected = mapper.writeValueAsString(championsLeague);

            // when
            String actual = JsonConverter.toJson(championsLeague);

            // then
            assertEquals(expected, actual);
        }

    }


}