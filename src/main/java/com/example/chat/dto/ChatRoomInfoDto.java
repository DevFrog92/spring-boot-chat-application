package com.example.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.example.chat.dto.ChatMessageType.INFO;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomInfoDto {
    private ChatMessageType type;
    private Long roomId;
    private Integer participationNum;

    public ChatRoomInfoDto(Long roomId, Integer participationNum) {
        this.type = INFO;
        this.roomId = roomId;
        this.participationNum = participationNum;
    }
}
