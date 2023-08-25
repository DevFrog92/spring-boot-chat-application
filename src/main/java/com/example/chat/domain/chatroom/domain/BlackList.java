package com.example.chat.domain.chatroom.domain;

import com.example.chat.domain.member.domain.Member;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class BlackList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "chat_room")
    private Room room;
    @ManyToOne
    @JoinColumn(name = "ban_member")
    private Member member;
    @CreatedDate
    private LocalDateTime registrationAt;

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
                ", banedAt=" + registrationAt +
                '}';
    }
}
