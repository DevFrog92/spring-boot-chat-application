package com.example.chat.domain;

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
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "member")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "room")
    private Room room;

    @Builder
    public ParticipationRoom(Member member, Room room) {
        this.member = member;
        this.room = room;
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
