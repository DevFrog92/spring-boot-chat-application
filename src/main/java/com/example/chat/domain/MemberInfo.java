package com.example.chat.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class MemberInfo {
    private Long id;
    private String name;
    private String token;

    @Builder
    public MemberInfo(Long id, String name, String token) {
        this.id = id;
        this.name = name;
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
