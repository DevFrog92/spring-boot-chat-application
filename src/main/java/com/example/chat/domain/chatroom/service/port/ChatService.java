package com.example.chat.domain.chatroom.service.port;

import com.example.chat.domain.chatroom.dto.message.ChatMessageDto;
import com.example.chat.domain.chatroom.dto.message.ChatMessageType;

public interface ChatService {
    void sendChatMessage(ChatMessageDto dto);

    void sendEnterChatRoomMessage(Long roomId, String name);

    void sendLeaveChatRoomMessage(Long roomId, String name);

    void sendMessageToMemberTopic(Long memberId, ChatMessageType type);

    void sendChatRoomDeleted(Long roomId);

    void sendChatRoomInfo(Long roomId, Integer participationNum);
}
