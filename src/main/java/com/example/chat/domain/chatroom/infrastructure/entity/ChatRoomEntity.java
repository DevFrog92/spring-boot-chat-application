package com.example.chat.domain.chatroom.infrastructure.entity;

import com.example.chat.domain.chatroom.domain.ChatRoom;
import com.example.chat.domain.chatroom.domain.ChatRoomType;
import com.example.chat.domain.member.infrastructure.entity.MemberEntity;
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
@SQLDelete(sql = "update rooms set disabled_at = CURRENT_TIMESTAMP where id = ?")
@Where(clause = "disabled_at IS NULL")
@Table(name = "rooms")
public class ChatRoomEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "is_private")
    private Boolean isPrivate;
    @Column(name = "secret_code")
    private String secretCode;
    @Column(name = "max_chat_room_size")
    private Integer maxChatRoomSize;
    @Column(name = "participation_num")
    private Integer participationNum;
    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "disabled_at")
    private LocalDateTime disabledAt;
    @ManyToOne
    @JoinColumn(name = "member")
    private MemberEntity member;


    public static ChatRoomEntity fromModel(ChatRoom chatRoom) {
        ChatRoomEntity room = new ChatRoomEntity();
        room.id = chatRoom.getId();
        room.name = chatRoom.getName();
        room.isPrivate = chatRoom.getType().equals(ChatRoomType.PRIVATE);
        room.secretCode = chatRoom.getSecretCode();
        room.maxChatRoomSize = chatRoom.getMaxChatRoomSize();
        room.participationNum = chatRoom.getParticipationNum();
        room.member = MemberEntity.fromModel(chatRoom.getMember());
        return room;
    }

    public ChatRoom toModel() {
        return ChatRoom.builder()
                .id(id)
                .name(name)
                .type(isPrivate ? ChatRoomType.PRIVATE : ChatRoomType.PUBLIC)
                .secretCode(secretCode)
                .maxChatRoomSize(maxChatRoomSize)
                .participationNum(participationNum)
                .createdAt(createdAt)
                .disabledAt(disabledAt)
                .member(member.toModel())
                .build();
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
