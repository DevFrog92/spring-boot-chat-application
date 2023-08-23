package com.example.chat.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class BlackList {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "chat_room")
    private Room room;
    @ManyToOne
    @JoinColumn(name = "ban_member")
    private Member member;

    @Builder
    public BlackList(Room room, Member member) {
        this.room = room;
        this.member = member;
    }

    @Override
    public String toString() {
        return "BlackList{" +
                "id=" + id +
                ", room=" + room +
                ", member=" + member +
                '}';
    }
}
