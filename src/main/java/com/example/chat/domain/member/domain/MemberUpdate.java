package com.example.chat.domain.member.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberUpdate {
    private final String nickname;

    @Builder
    public MemberUpdate(
            @JsonProperty("nickname") String nickname) {
        this.nickname = nickname;
    }
}
