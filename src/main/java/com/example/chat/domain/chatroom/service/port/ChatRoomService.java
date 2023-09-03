package com.example.chat.domain.chatroom.service.port;

import com.example.chat.domain.chatroom.domain.ChatRoom;
import com.example.chat.domain.chatroom.dto.ChatRoomCreateDto;
import com.example.chat.domain.member.domain.Member;

import java.util.List;

public interface ChatRoomService {
    ChatRoom getById(Long id);

    ChatRoom create(Member member, ChatRoomCreateDto chatRoomCreateDto);

    void updateParticipationNum(ChatRoom chatRoom);

    List<ChatRoom> findAllRooms(Member member);
    void deleteRoom(Member member, ChatRoom chatRoom);
}
