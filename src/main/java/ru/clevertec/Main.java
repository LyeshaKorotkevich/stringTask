package ru.clevertec;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import ru.clevertec.model.ChampionsLeague;
import ru.clevertec.model.Player;
import ru.clevertec.model.Team;
import ru.clevertec.util.JsonConverter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Gson gson = new Gson();
        Player playerA1 = new Player();
        playerA1.setId(UUID.randomUUID());
        playerA1.setName(null);
        playerA1.setSurname("Surname A1");
        playerA1.setDateBirth(LocalDate.of(1990, 1, 1));
        playerA1.setNumber(7);

        Player playerA2 = new Player();
        playerA2.setId(UUID.randomUUID());
        playerA2.setName("Player A2");
        playerA2.setSurname("Surname A2");
        playerA2.setDateBirth(LocalDate.of(1992, 5, 15));
        playerA2.setNumber(10);

        Player playerA3 = new Player();
        playerA3.setId(UUID.randomUUID());
        playerA3.setName("Player A3");
        playerA3.setSurname("Surname A3");
        playerA3.setDateBirth(LocalDate.of(1988, 11, 30));
        playerA3.setNumber(5);

        Player playerA4 = new Player();
        playerA4.setId(UUID.randomUUID());
        playerA4.setName("Player A4");
        playerA4.setSurname("Surname A4");
        playerA4.setDateBirth(LocalDate.of(1993, 7, 20));
        playerA4.setNumber(9);

        Team teamA1 = new Team();
        teamA1.setId(UUID.randomUUID());
        teamA1.setName("Team A1");
        teamA1.setCountry("Country A");
        teamA1.setPlayers(Arrays.asList(playerA1, playerA2));

        Team teamA2 = new Team();
        teamA2.setId(UUID.randomUUID());
        teamA2.setName("Team A2");
        teamA2.setCountry("Country A");
        teamA2.setPlayers(Arrays.asList(playerA3, playerA4));

        Player playerB1 = new Player();
        playerB1.setId(UUID.randomUUID());
        playerB1.setName("Player B1");
        playerB1.setSurname("Surname B1");
        playerB1.setDateBirth(LocalDate.of(1991, 4, 10));
        playerB1.setNumber(8);

        Player playerB2 = new Player();
        playerB2.setId(UUID.randomUUID());
        playerB2.setName("Player B2");
        playerB2.setSurname("Surname B2");
        playerB2.setDateBirth(LocalDate.of(1989, 8, 25));
        playerB2.setNumber(11);

        Player playerB3 = new Player();
        playerB3.setId(UUID.randomUUID());
        playerB3.setName("Player B3");
        playerB3.setSurname("Surname B3");
        playerB3.setDateBirth(LocalDate.of(1994, 2, 28));
        playerB3.setNumber(6);

        Player playerB4 = new Player();
        playerB4.setId(UUID.randomUUID());
        playerB4.setName("Player B4");
        playerB4.setSurname("Surname B4");
        playerB4.setDateBirth(LocalDate.of(1995, 9, 5));
        playerB4.setNumber(12);

        Team teamB1 = new Team();
        teamB1.setId(UUID.randomUUID());
        teamB1.setName("Team B1");
        teamB1.setCountry("Country B");
        teamB1.setPlayers(Arrays.asList(playerB1, playerB2));

        Team teamB2 = new Team();
        teamB2.setId(UUID.randomUUID());
        teamB2.setName("Team B2");
        teamB2.setCountry("Country B");
        teamB2.setPlayers(Arrays.asList(playerB3, playerB4));

        ChampionsLeague championsLeague = new ChampionsLeague();
        championsLeague.setId(UUID.randomUUID());
        championsLeague.setDateStart(OffsetDateTime.parse("2023-11-07T12:34:56Z"));
        Map<Character, List<Team>> groups = new HashMap<>();
        groups.put('A', Arrays.asList(teamA1, teamA2));
        groups.put('B', Arrays.asList(teamB1, teamB2));
        championsLeague.setGroups(groups);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        try {
            String str = mapper.writeValueAsString(playerA1);
            Player player = mapper.readValue(str, Player.class);
            System.out.println(player);
            Player player1 = (Player) JsonConverter.fromJson(str, Player.class);
            System.out.println(player1);
            //TODO не работет с null
//            String str = mapper.writeValueAsString(teamA1);
//            Team player = mapper.readValue(str, Team.class);
//            System.out.println(player);
//            Team player1 = (Team) JsonConverter.fromJson(str, Team.class);
//            System.out.println(player1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}