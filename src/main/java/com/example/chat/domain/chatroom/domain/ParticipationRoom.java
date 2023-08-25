package com.example.chat.domain.chatroom.domain;

import com.example.chat.domain.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ParticipationRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "member")
    private Member member;
    @ManyToOne
    @JoinColumn(name = "room")
    private Room room;
    private Boolean submitKey = false;
    private Boolean joined = false;

    @Builder
    public ParticipationRoom(Member member, Room room, Boolean submitKey, Boolean joined) {
        this.member = member;
        this.room = room;
        this.submitKey = submitKey;
        this.joined = joined;
    }

    public void setFirstJoin(Boolean joined) {
        this.joined = joined;
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
