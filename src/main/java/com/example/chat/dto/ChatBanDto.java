package com.example.chat.dto;

import com.example.chat.domain.MemberInfo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatBanDto {
    private ChatMessageType type;
    private Long roomId;
    private MemberInfo requestMemberInfo;
    private MemberInfo banMemberInfo;
    private Long requestMemberId;
    private String banMemberName;
    private Long banMemberId;

    @Override
    public String toString() {
        return "ChatBanDto{" +
                "type=" + type +
                ", roomId=" + roomId +
                ", memberInfo=" + requestMemberInfo +
                ", banUserName='" + banMemberName + '\'' +
                '}';
    }
}
