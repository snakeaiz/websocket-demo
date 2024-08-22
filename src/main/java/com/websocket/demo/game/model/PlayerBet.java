package com.websocket.demo.game.model;

import lombok.*;
@Getter
@Setter
public class PlayerBet {
    private String playerName;
    private int number;
    private double amount;
    private double winnings;
    private double balance;

    public double getWinnings() {
        return winnings;
    }

    public double getAmount() {
        return amount;
    }

    public int getNumber() {
        return number;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setWinnings(double winnings) {
        this.winnings = winnings;
    }

    public PlayerBet(String playerName, int number, double amount) {
        this.playerName = playerName;
        this.number = number;
        this.amount = amount;
        this.winnings = 0;
        this.balance = 10000;
    }
}
