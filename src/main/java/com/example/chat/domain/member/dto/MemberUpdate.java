package com.example.chat.domain.member.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberUpdate {
    private String nickname;

    @Builder
    public MemberUpdate(String nickname) {
        this.nickname = nickname;
    }
}
