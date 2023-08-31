package com.example.chat.domain.common.service.port;

import com.example.chat.domain.chatroom.dto.message.ChatMessage;

public interface RedisPublish {
    void publish(String topic, ChatMessage message);
}
