package com.example.chat.domain.chatroom.service.port;

import com.example.chat.domain.chatroom.domain.ChatRoom;
import com.example.chat.domain.member.domain.Member;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository {

    Optional<ChatRoom> getById(long id);

    List<ChatRoom> findAllRooms(Member member);

    ChatRoom save(ChatRoom chatRoom);

    void deletById(long id);

}
