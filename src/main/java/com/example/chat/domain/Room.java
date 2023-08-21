package com.example.chat.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String roomName;
    private Boolean isPrivate;
    private String secretCode;
    private Integer maxPoolSize;
    private Integer participationNum;
    @CreatedDate
    private LocalDateTime createdAt;
    @CreatedDate
    private LocalDateTime disabledAt;
    @ManyToOne
    @JoinColumn(name = "member")
    private Member member;

    @Builder
    public Room(String roomName,
                Boolean isPrivate,
                String secretCode,
                Integer maxPoolSize,
                Integer participationNum,
                Member member) {
        this.roomName = roomName;
        this.isPrivate = isPrivate;
        this.secretCode = secretCode;
        this.maxPoolSize = maxPoolSize;
        this.participationNum = participationNum;
        this.member = member;
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", roomName='" + roomName + '\'' +
                ", isPrivate=" + isPrivate +
                ", secretCode='" + secretCode + '\'' +
                ", maxPoolSize=" + maxPoolSize +
                ", participationNum=" + participationNum +
                ", createdAt=" + createdAt +
                ", disabledAt=" + disabledAt +
                ", member=" + member +
                '}';
    }
}
