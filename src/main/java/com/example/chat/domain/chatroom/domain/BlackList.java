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
    public BlackList(final Long id,
                     final ChatRoom room,
                     final Member member,
                     final LocalDateTime registrationAt) {
        this.id = id;
        this.room = room;
        this.member = member;
        this.registrationAt = registrationAt;
    }

    public static BlackList create(final Member member,
                                   final ChatRoom room) {
        return BlackList.builder()
                .room(room)
                .member(member)
                .build();
    }
}
