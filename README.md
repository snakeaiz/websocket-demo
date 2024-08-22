#Websocket Demo Project<br>
#Author: Pavel Sulga<br>
#UI: http://localhost:8080/index.html<br>
#WS: ws://localhost:8080/ws<br>

#Project is based on Java 17, Gradle 11<br>
#Srping Boot 3+<br>
#Spring Websocket (No STOMP, defining custom WebSocketMessageHandler)<br>
#Srping Redis<br>

Idea: Build up a single Guess number game, make use of client-server websocket communication (Non-STOMP), Redis, which would be chacing values, keeping them (for instance if one of the websocket server instances will get down, it will re-connect and get all stored values from redis pool, which will guarantee robust work).<br>

TO-DO: Add docker compose, add redis logic to work with pool and caching mechanism, build up a horizontally scalable application to start up several websocket-server instances which would help keep it robust (i.e. docker-compose up --scale websocket-server=3), add repository and db acces, customize some user-based WebSocket logic as currently it just broadcast across all channels, customize redis logic.<br>



Websocket url: ws://localhost:8080/ws<br>
Simple UI: http://localhost:8080/index.html<br>



