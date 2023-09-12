package com.example.chat.domain.chatroom.dto.message;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessageDto implements ChatMessage {
    private ChatMessageType type;
    private Long roomId;
    private String sender;
    private String message;

    @Builder
    public ChatMessageDto(final ChatMessageType type,
                          final Long roomId,
                          final String sender,
                          final String message) {
        this.type = type;
        this.roomId = roomId;
        this.sender = sender;
        this.message = message;
    }
}
