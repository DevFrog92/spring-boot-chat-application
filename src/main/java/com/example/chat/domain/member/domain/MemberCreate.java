package com.example.chat.domain.member.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberCreate {
    private final String name;
    private final String nickName;

    @Builder
    public MemberCreate(
            @JsonProperty("name") String name,
            @JsonProperty("nickname") String nickName) {
        this.name = name;
        this.nickName = nickName;
    }
}
