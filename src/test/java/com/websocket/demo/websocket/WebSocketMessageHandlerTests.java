package com.websocket.demo.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.websocket.demo.game.model.PlayerBet;
import com.websocket.demo.game.service.GameService;
import com.websocket.demo.redis.RedisPublisher;
import com.websocket.demo.redis.service.RedisService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

class WebSocketMessageHandlerTests {
    @Mock
    private RedisPublisher redisPublisher;
    @Mock
    private GameService gameService;
    @Mock
    private RedisService redisService;
    @Mock
    private WebSocketSession session;
    @Mock
    private ObjectMapper objectMapper;
    @InjectMocks
    private GameWebSocketMessageHandler webSocketMessageHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
    }

    @Test
    void testAfterConnectionEstablished() throws IOException, InterruptedException {
        webSocketMessageHandler.afterConnectionEstablished(session);

        verify(session, times(1)).sendMessage(new TextMessage("Place your bets"));
        verify(session, times(1)).sendMessage(new TextMessage("Rounds starts in 10s"));
        // verify that the session is added to the sessions set
        assert(webSocketMessageHandler.getSessions().contains(session));
    }

    @Test
    void testHandleTextMessage() throws JsonProcessingException {
        // Arrange
        String messagePayload = "{\"playerName\":\"John\", \"number\":\"7\", \"amount\":\"100\"}";
        TextMessage message = new TextMessage(messagePayload);
        Map<String, Object> payload = new HashMap<>();
        payload.put("playerName", "John");
        payload.put("number", "7");
        payload.put("amount", "100");

        webSocketMessageHandler.handleTextMessage(session, message);

        verify(gameService, times(1)).placeBet("John", 7, 100.0);
        verify(redisPublisher, times(1)).publish("chat", gameService.getAllBets().toString());
        verify(redisService, times(1)).saveObject(anyString(), anyString());
    }

    @Test
    void testStartRound() throws IOException {
        webSocketMessageHandler.startRound();

        verify(gameService, times(1)).startRound();
    }

    @Test
    void testEndRound() throws IOException {
        PlayerBet playerBet = new PlayerBet("John", 7, 100.0);
        List<PlayerBet> winners = new ArrayList<>();
        winners.add(playerBet);

        when(gameService.determineWinners()).thenReturn(winners);
        when(gameService.getWinningNumber()).thenReturn(7);

        webSocketMessageHandler.endRound();

        verify(gameService, times(1)).endRound();
    }

    @Test
    void testAfterConnectionClosed() throws IOException, InterruptedException {
        webSocketMessageHandler.afterConnectionEstablished(session);

        webSocketMessageHandler.afterConnectionClosed(session, null);

        // Assert and Verify that the session is removed from the sessions set
        assert(!webSocketMessageHandler.getSessions().contains(session));
    }

    @Test
    void testBroadcast() throws IOException, InterruptedException {
        webSocketMessageHandler.afterConnectionEstablished(session);
        String broadcastMessage = "Place your bets";

        webSocketMessageHandler.broadcast(broadcastMessage);

        verify(session, times(1)).sendMessage(new TextMessage(broadcastMessage));
    }
}