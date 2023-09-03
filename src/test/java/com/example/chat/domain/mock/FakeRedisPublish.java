package com.example.chat.domain.mock;

import com.example.chat.domain.chatroom.dto.message.ChatMessage;
import com.example.chat.domain.common.service.port.RedisPublish;

import java.util.HashMap;
import java.util.Map;

public class FakeRedisPublish implements RedisPublish {
    private final Map<String, Object> fakeTemplate = new HashMap<>();

    @Override
    public void publish(final String topic, final ChatMessage message) {
        fakeTemplate.put(topic, message);
    }


    public Map<String, Object> getData(final String topic) {
        return new HashMap<>(fakeTemplate);
    }
}
