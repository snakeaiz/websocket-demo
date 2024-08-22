package com.websocket.demo.redis;

import com.websocket.demo.websocket.GameWebSocketMessageHandler;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

@Service
public class RedisSubscriber implements MessageListener {

    private final GameWebSocketMessageHandler gameWebSocketMessageHandler;

    public RedisSubscriber(@Lazy GameWebSocketMessageHandler gameWebSocketMessageHandler) {
        this.gameWebSocketMessageHandler = gameWebSocketMessageHandler;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String payload = new String(message.getBody());

        // Broadcast the received message to all WebSocket clients
        gameWebSocketMessageHandler.broadcast(payload);
    }
}
