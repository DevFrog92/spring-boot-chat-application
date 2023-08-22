package com.example.chat.dto;

import com.example.chat.domain.MemberInfo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatBanDto {
    private ChatMessageType type;
    private Long roomId;
    private MemberInfo loginInfo;
    private String banUserName;

    @Override
    public String toString() {
        return "ChatBanDto{" +
                "type=" + type +
                ", roomId=" + roomId +
                ", loginInfo=" + loginInfo +
                ", banUserName='" + banUserName + '\'' +
                '}';
    }
}
