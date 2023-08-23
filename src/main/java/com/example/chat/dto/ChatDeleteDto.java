package com.example.chat.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class ChatDeleteDto {
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
