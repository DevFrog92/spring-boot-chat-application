package com.example.chat.domain.chatroom.domain;

import com.example.chat.domain.member.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@NoArgsConstructor
public class ParticipationRoom {
    private Long id;
    private Member member;
    private ChatRoom chatRoom;
    private Boolean submitKey;
    private Boolean joined;

    @Builder
    public ParticipationRoom(Long id,
                             Member member,
                             ChatRoom chatRoom,
                             Boolean submitKey,
                             Boolean joined) {
        this.id = id;
        this.member = member;
        this.chatRoom = chatRoom;
        this.submitKey = submitKey;
        this.joined = joined;
    }

    public static ParticipationRoom create(Member member,
                                           ChatRoom chatRoom) {
        return ParticipationRoom.builder()
                .member(member)
                .chatRoom(chatRoom)
                .submitKey(!chatRoom.getType().equals(ChatRoomType.PRIVATE))
                .joined(false)
                .build();
    }

    public ParticipationRoom join(Member member,
                                  ChatRoom chatRoom) {
        return ParticipationRoom.builder()
                .id(id)
                .member(member)
                .chatRoom(chatRoom.join())
                .submitKey(submitKey)
                .joined(true)
                .build();
    }

    public ParticipationRoom certificate(String secretCode) {
        if (!Objects.equals(this.chatRoom.getSecretCode(), secretCode)) {
            throw new RuntimeException("The code is bad! "); // fixme
        }

        return ParticipationRoom.builder()
                .id(id)
                .member(member)
                .chatRoom(chatRoom)
                .submitKey(true)
                .joined(false)
                .build();
    }

    public boolean getJoinState() {
        return this.joined;
    }

    public boolean isCertified() {
        return this.submitKey;
    }
}
