package com.example.chat.domain.chatroom.dto.message;

import com.example.chat.domain.chatroom.dto.message.ChatMessage;
import com.example.chat.domain.chatroom.dto.message.ChatMessageType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatNoticeDto implements ChatMessage {
    private ChatMessageType type;
    private Long roomId;
    private Long participationId;

    @Override
    public String toString() {
        return "ChatEnterDto{" +
                "type=" + type +
                ", roomId=" + roomId +
                ", participationId=" + participationId +
                '}';
    }
}
