package com.websocket.demo.service;

import com.websocket.demo.game.model.PlayerBet;
import com.websocket.demo.game.service.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTests {

    private GameService gameService;

    @BeforeEach
    void setUp() {
        gameService = new GameService();
    }

    @Test
    void testPlaceBetValidBet() {
        String playerName = "John";
        int number = 7;
        double amount = 100.0;

        gameService.placeBet(playerName, number, amount);

        Map<String, PlayerBet> bets = gameService.getAllBets();
        assertEquals(1, bets.size());
        assertTrue(bets.containsKey(playerName));
        assertEquals(number, bets.get(playerName).getNumber());
        assertEquals(amount, bets.get(playerName).getAmount());
    }

    @Test
    void testPlaceBetInvalidBetNumberOutOfRange() {
        String playerName = "John";
        int number = 11;
        double amount = 100.0;

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            gameService.placeBet(playerName, number, amount);
        });

        assertEquals("Invalid bet", thrown.getMessage());
    }

    @Test
    void testPlaceBetInvalidBetNegativeAmount() {
        String playerName = "John";
        int number = 5;
        double amount = -10.0;

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            gameService.placeBet(playerName, number, amount);
        });

        assertEquals("Invalid bet", thrown.getMessage());
    }

    @Test
    void testStartRound() {
        gameService.placeBet("John", 7, 100.0);

        gameService.startRound();

        assertTrue(gameService.getAllBets().isEmpty());
    }

    @Test
    void testEndRound() {
        gameService.endRound();

        int winningNumber = gameService.getWinningNumber();
        assertTrue(winningNumber >= 1 && winningNumber <= 10, "Winning number should be between 1 and 10");
    }

    @Test
    void testDetermineWinners() {
        // Arrange
        // Since the winning number is random, we simulate the process.
        gameService.placeBet("John", 10, 100.0);
        gameService.placeBet("Jane", 7, 200.0);

        // Act
        gameService.endRound();
        List<PlayerBet> winners = gameService.determineWinners();

        // Assert
        int winningNumber = gameService.getWinningNumber();
        if (winningNumber == 10) {
            assertEquals(1, winners.size());
            assertEquals("John", winners.get(0).getPlayerName());
            assertEquals(990.0, winners.get(0).getWinnings());
        } else if (winningNumber == 7) {
            assertEquals(1, winners.size());
            assertEquals("Jane", winners.get(0).getPlayerName());
            assertEquals(1980.0, winners.get(0).getWinnings());
        } else {
            assertTrue(winners.isEmpty(), "There should be no winners if the winning number doesn't match any bet.");
        }
    }

    @Test
    void testDetermineWinnersNoWinners() {
        gameService.placeBet("John", 5, 100.0);
        gameService.placeBet("Jane", 7, 200.0);
        gameService.endRound();

        List<PlayerBet> winners = gameService.determineWinners();

        assertTrue(winners.isEmpty());
    }

    @Test
    void testGetAllBets() {
        gameService.placeBet("John", 7, 100.0);
        gameService.placeBet("Jane", 8, 200.0);

        Map<String, PlayerBet> bets = gameService.getAllBets();

        assertEquals(2, bets.size());
        assertEquals(100.0, bets.get("John").getAmount());
        assertEquals(200.0, bets.get("Jane").getAmount());
    }
}
