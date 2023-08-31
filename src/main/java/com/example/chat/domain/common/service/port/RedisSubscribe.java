package com.example.chat.domain.common.service.port;

public interface RedisSubscribe {
    void sendMessage(String publishMessage);

    void sendChatRoomInfo(String publishMessage);

    void sendMessageToMember(String publishMessage);
}
