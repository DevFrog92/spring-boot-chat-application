package com.example.chat.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessageDto {
    private ChatMessageType type;
    private String roomId;
    private String sender;
    private String message;
}
