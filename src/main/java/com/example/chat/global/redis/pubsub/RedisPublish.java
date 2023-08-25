package com.example.chat.global.redis.pubsub;

import com.example.chat.domain.chatroom.dto.message.ChatMessage;

public interface RedisPublish {
    void publish(String topic, ChatMessage message);
}
