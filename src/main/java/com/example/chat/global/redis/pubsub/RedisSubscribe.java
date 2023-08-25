package com.example.chat.global.redis.pubsub;

public interface RedisSubscribe {
    void sendMessage(String publishMessage);

    void sendChatRoomInfo(String publishMessage);

    void sendMessageToMember(String publishMessage);
}
