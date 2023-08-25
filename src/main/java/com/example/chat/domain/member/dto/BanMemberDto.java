package com.example.chat.domain.member.dto;

import com.example.chat.domain.chatroom.dto.message.ChatMessageType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BanMemberDto {
    private ChatMessageType type;
    private Long roomId;
    private Long requestMemberId;
    private String banMemberName;
    private Long banMemberId;

    @Override
    public String toString() {
        return "ChatBanDto{" +
                "type=" + type +
                ", roomId=" + roomId +
                ", banUserName='" + banMemberName + '\'' +
                '}';
    }
}
