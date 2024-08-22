package com.websocket.demo.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.websocket.demo.game.model.PlayerBet;
import com.websocket.demo.game.service.GameService;
import com.websocket.demo.redis.RedisPublisher;
import com.websocket.demo.redis.service.RedisService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class GameWebSocketMessageHandler extends TextWebSocketHandler {

    private final RedisPublisher redisPublisher;
    private final GameService gameService;
    private final RedisService redisService;
    private static final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RedisTemplate<String, Object> redisTemplate;

    public GameWebSocketMessageHandler(RedisPublisher redisPublisher, GameService gameService, RedisService redisService, RedisTemplate<String, Object> redisTemplate) {
        this.redisPublisher = redisPublisher;
        this.gameService = gameService;
        this.redisService = redisService;
        this.redisTemplate = redisTemplate;
    }

    public Set<WebSocketSession> getSessions() {
        return sessions;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws IOException, InterruptedException {
        sessions.add(session);
        session.sendMessage(new TextMessage("Place your bets"));
        session.sendMessage(new TextMessage("Rounds starts in 10s"));
//        gameService.startRound();
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws JsonProcessingException {
        Map<String, Object> payload = objectMapper.readValue(message.getPayload(), Map.class);

        String playerName = (String) payload.get("playerName");
        int playerNum = Integer.parseInt((String) payload.get("number"));
        double playerAmount = Double.parseDouble((String) payload.get("amount"));

//        System.out.println(playerName + " " + playerNum + " " + playerAmount);

        gameService.placeBet(playerName, playerNum, playerAmount);

        redisPublisher.publish("chat", gameService.getAllBets().toString());

//        Map<String, Object> playerBet = (Map<String, Object>) payload.get("playerBet");
//        System.out.println(playerBet);
//        int number = (int) payload.get("playerBet.number");
//        System.out.println(number);
//        double amount = (double) payload.get("amount");
//        gameService.placeBet(playerName, number, amount);
        //publish received message to Redis channel
        int i = 0;
        redisService.saveObject(String.valueOf(++i),message.toString());
//        redisPublisher.publish("chat", message.getPayload());
//        redisPublisher.publish("chat", gameService.getAllBets().toString());
//        redisPublisher.publish("chat", (String) redisService.getObject(String.valueOf(1)));
//        System.out.println((String) redisService.getObject(String.valueOf(1)));
    }

    public void startRound() throws IOException {
        gameService.startRound();
        // Notify all players that a new round has started
        for (WebSocketSession session : sessions) {
            session.sendMessage(new TextMessage("Round started!"));
        }
    }

    public void endRound() throws IOException {
        gameService.endRound();
        List<PlayerBet> winners = gameService.determineWinners();
        List<String> winnerNames = new ArrayList<>();
        for (PlayerBet bet : winners) {
            winnerNames.add(bet.getPlayerName());
        }

        // Notify all players of the winning number and the winners
        for (WebSocketSession session : sessions) {
            session.sendMessage(new TextMessage("Winning number: " + gameService.getWinningNumber()));
            session.sendMessage(new TextMessage("Winners: " + objectMapper.writeValueAsString(winnerNames)));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws IOException {
        sessions.remove(session);
    }

    // Broadcast the message to all connected websocket sessions
    public void broadcast(String message) {
        sessions.forEach(session -> {
            if (session.isOpen()) {
                try {
                    session.sendMessage(new TextMessage(message));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void replyToSession(String message) {
        sessions.forEach(session -> {
            if (session.isOpen()) {
                String user = session.getPrincipal().getName();
            }
        });
    }
}
