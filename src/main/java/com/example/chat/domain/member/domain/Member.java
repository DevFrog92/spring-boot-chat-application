package com.example.chat.domain.member.domain;

import com.example.chat.domain.member.dto.MemberCreate;
import com.example.chat.domain.member.dto.MemberUpdate;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Member {
    private final Long id;
    private final String name;
    private final String nickname;

    @Builder
    public Member(final Long id,
                  final String name,
                  final String nickname) {
        this.id = id;
        this.name = name;
        this.nickname = nickname;
    }

    public static Member from(MemberCreate memberCreate) {
        return Member.builder()
                .name(memberCreate.getName())
                .nickname(memberCreate.getNickName())
                .build();
    }

    public Member update(MemberUpdate memberUpdate) {
        return Member.builder()
                .id(id)
                .name(name)
                .nickname(memberUpdate.getNickname())
                .build();
    }
}
