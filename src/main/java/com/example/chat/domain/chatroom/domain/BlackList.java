package com.example.chat.domain.chatroom.domain;

import com.example.chat.domain.member.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class BlackList {
    private Long id;
    private ChatRoom room;
    private Member member;
    private LocalDateTime registrationAt;

    @Builder
    public BlackList(Long id,
                     ChatRoom room,
                     Member member,
                     LocalDateTime registrationAt) {
        this.id = id;
        this.room = room;
        this.member = member;
        this.registrationAt = registrationAt;
    }

    public static BlackList create(Member member, ChatRoom room) {
        return BlackList.builder()
                .room(room)
                .member(member)
                .build();
    }
}
