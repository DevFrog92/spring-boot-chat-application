package com.example.chat.domain.chatroom.domain;

import com.example.chat.domain.member.domain.Member;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "update room set disabled_at = CURRENT_TIMESTAMP where id = ?")
@Where(clause = "disabled_at IS NULL")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Boolean isPrivate;
    private String secretCode;
    private Integer maxChatRoomSize;
    private Integer participationNum;
    @CreatedDate
    private LocalDateTime createdAt;
    private LocalDateTime disabledAt;
    @ManyToOne
    @JoinColumn(name = "member")
    private Member member;

    @Builder
    public Room(String name,
                Boolean isPrivate,
                String secretCode,
                Integer maxChatRoomSize,
                Integer participationNum,
                Member member) {
        this.name = name;
        this.isPrivate = isPrivate;
        this.secretCode = secretCode;
        this.maxChatRoomSize = maxChatRoomSize;
        this.participationNum = participationNum;
        this.member = member;
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", roomName='" + name + '\'' +
                ", isPrivate=" + isPrivate +
                ", secretCode='" + secretCode + '\'' +
                ", maxChatRoomSize=" + maxChatRoomSize +
                ", participationNum=" + participationNum +
                ", createdAt=" + createdAt +
                ", disabledAt=" + disabledAt +
                ", member=" + member +
                '}';
    }
}
