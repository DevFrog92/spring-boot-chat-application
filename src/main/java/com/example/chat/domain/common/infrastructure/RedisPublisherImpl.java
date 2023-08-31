package com.example.chat.domain.common.infrastructure;

import com.example.chat.domain.chatroom.dto.message.ChatMessage;
import com.example.chat.domain.common.service.port.RedisPublish;
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
