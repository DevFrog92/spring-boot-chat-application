package com.example.chat.domain.chatroom.dto.chatroom;

import com.example.chat.domain.member.domain.Member;
import com.example.chat.domain.chatroom.domain.Room;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class RoomDto {
    private Long id;
    private String name;
    private Boolean isPrivate;
    private String secretCode;
    private Integer maxChatRoomSize;
    private Integer participationNum;
    private LocalDateTime createdAt;
    private LocalDateTime disabledAt;
    private Member member;

    @Builder
    public RoomDto(Long id,
                   String name,
                   Boolean isPrivate,
                   String secretCode,
                   Integer maxChatRoomSize,
                   Integer participationNum,
                   LocalDateTime createdAt,
                   LocalDateTime disabledAt,
                   Member member) {
        this.id = id;
        this.name = name;
        this.isPrivate = isPrivate;
        this.secretCode = secretCode;
        this.maxChatRoomSize = maxChatRoomSize;
        this.participationNum = participationNum;
        this.createdAt = createdAt;
        this.disabledAt = disabledAt;
        this.member = member;
    }

    public static RoomDto fromEntity(Room room) {
        return RoomDto.builder()
                .id(room.getId())
                .name(room.getName())
                .isPrivate(room.getIsPrivate())
                .secretCode(room.getSecretCode())
                .maxChatRoomSize(room.getMaxChatRoomSize())
                .participationNum(room.getParticipationNum())
                .createdAt(room.getCreatedAt())
                .disabledAt(room.getDisabledAt())
                .member(room.getMember())
                .build();
    }

    public Room getEntity() {
        return new Room(
                this.id,
                this.name,
                this.isPrivate,
                this.secretCode,
                this.maxChatRoomSize,
                this.participationNum,
                this.createdAt,
                this.disabledAt,
                this.member
        );
    }
}
