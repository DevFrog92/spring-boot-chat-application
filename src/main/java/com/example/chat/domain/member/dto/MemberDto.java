package com.example.chat.domain.member.dto;

import com.example.chat.domain.member.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberDto {
    private Long id;
    private String name;
    private String nickname;

    @Builder
    public MemberDto(Long id,
                     String name,
                     String nickname) {
        this.id = id;
        this.name = name;
        this.nickname = nickname;
    }

    public static MemberDto fromEntity(Member member) {
        return MemberDto.builder()
                .id(member.getId())
                .name(member.getName())
                .nickname(member.getNickname())
                .build();
    }

    public Member getEntity() {
        return new Member(this.id, this.name, this.nickname);
    }
}
