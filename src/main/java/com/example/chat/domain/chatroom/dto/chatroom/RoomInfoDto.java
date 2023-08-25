package com.example.chat.domain.chatroom.dto.chatroom;

import com.example.chat.domain.chatroom.domain.Room;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.example.chat.domain.chatroom.dto.chatroom.ChatRoomType.PRIVATE;
import static com.example.chat.domain.chatroom.dto.chatroom.ChatRoomType.PUBLIC;

@Getter
@Setter
@NoArgsConstructor
public class RoomInfoDto {
    private Long id;
    private String name;
    private String ownerName;
    private ChatRoomType type;
    private Integer maxChatRoomSize;
    private Integer participationNum;

    @Builder
    public RoomInfoDto(Long id,
                       String name,
                       String ownerName,
                       Boolean isPrivate,
                       Integer maxChatRoomSize,
                       Integer participationNum) {
        this.id = id;
        this.name = name;
        this.ownerName = ownerName;
        this.type = isPrivate ? PRIVATE : PUBLIC;
        this.maxChatRoomSize = maxChatRoomSize;
        this.participationNum = participationNum;
    }

    public static RoomInfoDto fromEntity(Room room) {
        return RoomInfoDto.builder()
                .id(room.getId())
                .name(room.getName())
                .ownerName(room.getMember().getName())
                .isPrivate(room.getIsPrivate())
                .maxChatRoomSize(room.getMaxChatRoomSize())
                .participationNum(room.getParticipationNum())
                .build();
    }

    @Override
    public String toString() {
        return "RoomInfoDto{" +
                "id=" + id +
                ", roomName='" + name + '\'' +
                ", roomType=" + type +
                ", maxChatRoomSize=" + maxChatRoomSize +
                ", participationNum=" + participationNum +
                '}';
    }
}
