package com.example.chat.domain.chatroom.dto.chatroom;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateRoomDto {
    private Long requestMemberId;
    private String roomName;
    private ChatRoomType type;
    private String secretKey;

    @Override
    public String toString() {
        return "CreateRoomDto{" +
                "requestMemberId=" + requestMemberId +
                ", roomName='" + roomName + '\'' +
                ", type=" + type +
                ", secretKey='" + secretKey + '\'' +
                '}';
    }
}
