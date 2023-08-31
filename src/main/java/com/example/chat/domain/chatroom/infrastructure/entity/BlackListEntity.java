package com.example.chat.domain.chatroom.infrastructure.entity;

import com.example.chat.domain.chatroom.domain.BlackList;
import com.example.chat.domain.member.infrastructure.entity.MemberEntity;
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
public class BlackListEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "chat_room")
    private ChatRoomEntity room;
    @ManyToOne
    @JoinColumn(name = "ban_member")
    private MemberEntity member;
    @CreatedDate
    private LocalDateTime registrationAt;

    @Builder
    public BlackListEntity(ChatRoomEntity room,
                           MemberEntity memberEntity) {
        this.room = room;
        this.member = memberEntity;
    }

    public static BlackListEntity fromModel(BlackList blackList) {
        BlackListEntity blackListEntity = new BlackListEntity();
        blackListEntity.id = blackList.getId();
        blackListEntity.member = MemberEntity.fromModel(blackList.getMember());
        blackListEntity.room = ChatRoomEntity.fromModel(blackList.getRoom());
        blackListEntity.registrationAt = blackList.getRegistrationAt();
        return blackListEntity;
    }

    public BlackList toModel() {
        return BlackList.builder()
                .id(id)
                .member(member.toModel())
                .room(room.toModel())
                .registrationAt(registrationAt)
                .build();
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
