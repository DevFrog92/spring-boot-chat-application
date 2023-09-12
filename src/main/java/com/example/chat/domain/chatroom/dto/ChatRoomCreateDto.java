package com.example.chat.domain.chatroom.dto;

import com.example.chat.domain.chatroom.domain.ChatRoomType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomCreateDto {
    private Long requestMemberId;
    private String name;
    private ChatRoomType type;
    private String secretKey;
    private Integer maxChatRoomSize;

    @Builder
    public ChatRoomCreateDto(final Long requestMemberId,
                             final String name,
                             final ChatRoomType type,
                             final String secretKey,
                             final Integer maxPeopleAllowNum) {
        this.requestMemberId = requestMemberId;
        this.name = name;
        this.type = type;
        this.secretKey = secretKey;
        this.maxChatRoomSize = maxPeopleAllowNum;
    }
}
