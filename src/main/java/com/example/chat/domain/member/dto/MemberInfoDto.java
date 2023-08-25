package com.example.chat.domain.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberInfoDto {
    private Long id;
    private String name;
    private String nickname;
    private String token;

    @Builder
    public MemberInfoDto(Long id, String name, String nickname, String token) {
        this.id = id;
        this.name = name;
        this.nickname = nickname;
        this.token = token;
    }

    @Override
    public String toString() {
        return "LoginInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
