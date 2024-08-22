package com.websocket.demo.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisPublisher {
    private final RedisTemplate<String, String> redisTemplate;

    public RedisPublisher(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void publish(String topic, String message) {
        redisTemplate.convertAndSend(topic, message);
    }
}
