package com.example.chat.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDto {
    private ChatMessageType type;
    private Long roomId;
    private String sender;
    private String message;
    private long userCount;

    @Builder
    public ChatMessageDto(Long roomId, String sender, String message) {
        this.roomId = roomId;
        this.sender = sender;
        this.message = message;
    }
}
