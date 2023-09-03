package com.example.chat.domain.chatroom.dto;

import com.example.chat.domain.chatroom.domain.ChatRoomType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatRoomCreateDto {
    private Long requestMemberId;
    private String name;
    private ChatRoomType type;
    private String secretKey;
    private Integer maxChatRoomSize;

    @Builder
    public ChatRoomCreateDto(Long requestMemberId,
                             String name,
                             ChatRoomType type,
                             String secretKey,
                             Integer maxPeopleAllowNum) { // todo 변경
        this.requestMemberId = requestMemberId;
        this.name = name;
        this.type = type;
        this.secretKey = secretKey;
        this.maxChatRoomSize = maxPeopleAllowNum;
    }
}
