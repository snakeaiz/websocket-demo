# websocket-demo
Websocket Demo Project
Author: Pavel Sulga
UI: http://localhost:8080/index.html
WS: ws://localhost:8080/ws

Project is based on Java 17, Gradle 11
Srping Boot 3+
Spring Websocket (No STOMP, defining custom WebSocketMessageHandler)
Srping Redis

Idea: Build up a single Guess number game, make use of client-server websocket communication (Non-STOMP), Redis, which would be chacing values, keeping them (for instance if one of the websocket server instances will get down, it will re-connect and get all stored values from redis pool, which will guarantee robust work)

TO-DO: Add docker compose, add redis logic to work with pools, build up a scalable application to start up several websocket-server instances which would help to scale application horizontally (i.e. docker-compose up --scale websocket-server=3), add repository and db acces, customize some user-based WebSocket logic as currently it just broadcast across all channels, customize redis logic

Websocket url: ws://localhost:8080/ws
Simple UI to play with WebSocket: http://localhost:8080/index.html



