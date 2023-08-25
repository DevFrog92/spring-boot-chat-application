package com.example.chat.domain.chatroom.dto.message;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChatDeleteDto implements ChatMessage {
    private ChatMessageType type;
    private Long memberId;
    private Long roomId;

    @Builder
    public ChatDeleteDto(Long memberId, Long roomId) {
        this.memberId = memberId;
        this.roomId = roomId;
    }

    @Override
    public String toString() {
        return "ChatDeleteDto{" +
                "memberId=" + memberId +
                ", roomId=" + roomId +
                '}';
    }
}
