package com.example.chat.domain.chatroom.controller.dto.request;

import com.example.chat.domain.chatroom.dto.message.ChatMessageType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RequestDto {
    private ChatMessageType type;
    private Long roomId;
    private Long requestMemberId;

    @Builder
    public RequestDto(ChatMessageType type,
                      Long roomId,
                      Long requestMemberId) {
        this.type = type;
        this.roomId = roomId;
        this.requestMemberId = requestMemberId;
    }
}
