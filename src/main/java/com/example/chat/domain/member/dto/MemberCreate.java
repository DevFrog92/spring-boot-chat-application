package com.example.chat.domain.member.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberCreate {
    private final String name;
    private final String nickName;

    @Builder
    public MemberCreate(String name, String nickName) {
        this.name = name;
        this.nickName = nickName;
    }
}
