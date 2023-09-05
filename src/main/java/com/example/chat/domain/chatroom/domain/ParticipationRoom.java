package com.example.chat.domain.chatroom.domain;

import com.example.chat.domain.member.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ParticipationRoom {
    private Long id;
    private Member member;
    private ChatRoom chatRoom;
    private Boolean submitKey;
    private Boolean joined;

    @Builder
    public ParticipationRoom(final Long id,
                             final Member member,
                             final ChatRoom chatRoom,
                             final Boolean submitKey,
                             final Boolean joined) {
        this.id = id;
        this.member = member;
        this.chatRoom = chatRoom;
        this.submitKey = submitKey;
        this.joined = joined;
    }

    public static ParticipationRoom create(final Member member,
                                           final ChatRoom chatRoom) {
        return ParticipationRoom.builder()
                .member(member)
                .chatRoom(chatRoom)
                .submitKey(!chatRoom.getType().equals(ChatRoomType.PRIVATE))
                .joined(false)
                .build();
    }

    public ParticipationRoom join(final Member member,
                                  final ChatRoom chatRoom) {
        return ParticipationRoom.builder()
                .id(id)
                .member(member)
                .chatRoom(chatRoom.join())
                .submitKey(submitKey)
                .joined(true)
                .build();
    }

    public ParticipationRoom certification() {
        return ParticipationRoom.builder()
                .id(id)
                .member(member)
                .chatRoom(chatRoom)
                .submitKey(true)
                .joined(false)
                .build();
    }
}
