package com.example.chat.domain.chatroom.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatRoomCreate {
    private Long requestMemberId;
    private String name;
    private ChatRoomType type;
    private String secretKey;
    private Integer maxPeopleAllowNum;

    @Builder
    public ChatRoomCreate(Long requestMemberId,
                          String name,
                          ChatRoomType type,
                          String secretKey,
                          Integer maxPeopleAllowNum) {
        this.requestMemberId = requestMemberId;
        this.name = name;
        this.type = type;
        this.secretKey = secretKey;
        this.maxPeopleAllowNum = maxPeopleAllowNum;
    }
}
