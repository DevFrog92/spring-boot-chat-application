package com.example.chat.domain.chatroom.dto.message;

import lombok.*;

import static com.example.chat.domain.chatroom.dto.message.ChatMessageType.INFO;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomInfoDto implements ChatMessage {
    private ChatMessageType type;
    private Long roomId;
    private Integer participationNum;

    @Builder
    public ChatRoomInfoDto(final Long roomId,
                           final Integer participationNum) {
        this.type = INFO;
        this.roomId = roomId;
        this.participationNum = participationNum;
    }
}
