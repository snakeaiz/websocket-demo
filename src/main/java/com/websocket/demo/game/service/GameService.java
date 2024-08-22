package com.websocket.demo.game.service;

import com.websocket.demo.game.model.PlayerBet;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GameService {
    private final Map<String, PlayerBet> playerBetMap = new ConcurrentHashMap<>();
    private int winningNumber;

    public void placeBet(String playerName, int number, double amount) {
        if (number < 1 || number > 10 || amount <= 0) {
            throw new IllegalArgumentException("Invalid bet");
        }
        playerBetMap.put(playerName, new PlayerBet(playerName, number, amount));
    }

    public void startRound() {
        playerBetMap.clear();
    }

    public void endRound() {
        winningNumber = new Random().nextInt(10) + 1;
    }

    public List<PlayerBet> determineWinners() {
        List<PlayerBet> winners = new ArrayList<>();
        for (PlayerBet bet : playerBetMap.values()) {
            if (bet.getNumber() == winningNumber) {
                bet.setWinnings(bet.getAmount() * 9.9);
                winners.add(bet);
            }
        }
        return winners;
    }

    public Map<String, PlayerBet> getAllBets() {
        return playerBetMap;
    }

    public int getWinningNumber() {
        return winningNumber;
    }
}
