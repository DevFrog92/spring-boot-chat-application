package com.example.chat.domain.chatroom.infrastructure.entity;

import com.example.chat.domain.chatroom.domain.ParticipationRoom;
import com.example.chat.domain.member.infrastructure.entity.MemberEntity;
import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
@Table(name ="participation_room")
public class ParticipationRoomEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "member")
    private MemberEntity member;
    @ManyToOne
    @JoinColumn(name = "room")
    private ChatRoomEntity room;
    @Column(name = "submit_key")
    private Boolean submitKey = false;
    @Column(name = "joined")
    private Boolean joined = false;

    public static ParticipationRoomEntity from(ParticipationRoom participationRoom) {
        ParticipationRoomEntity participationRoomEntity = new ParticipationRoomEntity();
        participationRoomEntity.id = participationRoom.getId();
        participationRoomEntity.member = MemberEntity.fromModel(participationRoom.getMember());
        participationRoomEntity.room = ChatRoomEntity.fromModel(participationRoom.getChatRoom());
        participationRoomEntity.submitKey = participationRoom.getSubmitKey();
        participationRoomEntity.joined = participationRoom.getJoined();

        return participationRoomEntity;
    }

    public ParticipationRoom toModel() {
        return ParticipationRoom.builder()
                .id(id)
                .member(member.toModel())
                .chatRoom(room.toModel())
                .submitKey(submitKey)
                .joined(joined)
                .build();
    }

    @Override
    public String toString() {
        return "ParticipationRoom{" +
                "id=" + id +
                ", member=" + member +
                ", room=" + room +
                '}';
    }
}
