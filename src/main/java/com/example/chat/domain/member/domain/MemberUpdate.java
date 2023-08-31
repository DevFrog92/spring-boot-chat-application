package com.example.chat.domain.member.domain;

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
