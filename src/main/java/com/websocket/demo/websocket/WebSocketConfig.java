package com.websocket.demo.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final GameWebSocketMessageHandler webSocketMessageHandler;

    public WebSocketConfig(GameWebSocketMessageHandler gameWebSocketMessageHandler) {
        this.webSocketMessageHandler = gameWebSocketMessageHandler;
    }
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    registry.addHandler(webSocketMessageHandler, "/ws").setAllowedOrigins("*");
    }
}
