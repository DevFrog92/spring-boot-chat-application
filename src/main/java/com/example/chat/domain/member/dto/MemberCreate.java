package com.example.chat.domain.member.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberCreate {
    private String name;
    private String nickName;

    @Builder
    public MemberCreate(String name, String nickName) {
        this.name = name;
        this.nickName = nickName;
    }
}
