package com.example.chat.global.redis.pubsub;

import com.example.chat.domain.chatroom.dto.message.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisPublisherImpl implements RedisPublish {
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void publish(String topic, ChatMessage messageDto) {
        redisTemplate.convertAndSend(topic, messageDto);
    }
}
