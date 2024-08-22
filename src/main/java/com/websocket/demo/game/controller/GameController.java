package com.websocket.demo.game.controller;

import com.websocket.demo.game.service.GameService;
import com.websocket.demo.websocket.GameWebSocketMessageHandler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
public class GameController {

    private final GameWebSocketMessageHandler gameWebSocketMessageHandler;

    public GameController(GameWebSocketMessageHandler gameWebSocketMessageHandler, GameService gameService) {
        this.gameWebSocketMessageHandler = gameWebSocketMessageHandler;
    }

    @Scheduled(fixedDelay = 10000)
    public void manageGame() throws IOException, InterruptedException {
        gameWebSocketMessageHandler.startRound();
        Thread.sleep(10000);
        gameWebSocketMessageHandler.endRound();
    }
}
