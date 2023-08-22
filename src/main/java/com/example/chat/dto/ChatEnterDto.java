package com.example.chat.dto;

import com.example.chat.domain.MemberInfo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatEnterDto {
    private ChatMessageType type;
    private Long roomId;
    private MemberInfo loginInfo;

    @Override
    public String toString() {
        return "ChatEnterDto{" +
                "type=" + type +
                ", roomId=" + roomId +
                ", loginInfo=" + loginInfo +
                '}';
    }
}
