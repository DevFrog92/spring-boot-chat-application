package com.example.chat.domain.member.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberUpdate {
    private final String nickname;

    @Builder
    public MemberUpdate(String nickname) {
        this.nickname = nickname;
    }
}
