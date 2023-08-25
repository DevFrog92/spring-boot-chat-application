package com.example.chat.domain.chatroom.dto.message;

import com.example.chat.domain.chatroom.dto.message.ChatMessage;
import com.example.chat.domain.chatroom.dto.message.ChatMessageType;
import lombok.*;

import static com.example.chat.domain.chatroom.dto.message.ChatMessageType.INFO;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomInfoDto implements ChatMessage {
    private ChatMessageType type;
    private Long roomId;
    private Integer participationNum;

    @Builder
    public ChatRoomInfoDto(Long roomId, Integer participationNum) {
        this.type = INFO;
        this.roomId = roomId;
        this.participationNum = participationNum;
    }
}
