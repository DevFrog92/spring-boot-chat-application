package com.example.chat.domain.member.infrastructure.entity;

import com.example.chat.domain.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@Builder
@Table(name = "members")
@NoArgsConstructor
@AllArgsConstructor
public class MemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "nickname")
    private String nickname;

    public static MemberEntity fromModel(final Member member) {
        return MemberEntity.builder()
                .id(member.getId())
                .name(member.getName())
                .nickname(member.getNickname())
                .build();
    }

    public Member toModel() {
        return Member.builder()
                .id(id)
                .name(name)
                .nickname(nickname)
                .build();
    }
}
