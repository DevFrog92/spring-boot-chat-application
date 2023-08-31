package com.example.chat.domain.chatroom.controller.dto.response;


import com.example.chat.domain.chatroom.domain.ChatRoom;
import com.example.chat.domain.chatroom.domain.ChatRoomType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatRoomInfoResponse {
    private Long id;
    private String name;
    private String ownerName;
    private ChatRoomType type;
    private Integer maxPeopleAllowNum;
    private Integer participationNum;

    public static ChatRoomInfoResponse from(ChatRoom chatRoom) {
        return ChatRoomInfoResponse.builder()
                .id(chatRoom.getId())
                .name(chatRoom.getName())
                .ownerName(chatRoom.getMember().getName())
                .type(chatRoom.getType())
                .maxPeopleAllowNum(chatRoom.getMaxChatRoomSize())
                .participationNum(chatRoom.getParticipationNum())
                .build();
    }
}
