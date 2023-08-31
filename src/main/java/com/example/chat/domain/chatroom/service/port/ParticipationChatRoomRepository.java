package com.example.chat.domain.chatroom.service.port;

import com.example.chat.domain.chatroom.domain.ChatRoom;
import com.example.chat.domain.chatroom.domain.ParticipationRoom;
import com.example.chat.domain.member.domain.Member;

import java.util.Optional;

public interface ParticipationChatRoomRepository {
    Optional<ParticipationRoom> getByMemberAndRoom(Member member, ChatRoom chatRoom);

    void deleteByMemberAndRoom(Member member, ChatRoom chatRoom);

    void deleteAllByRoom(ChatRoom room);
    ParticipationRoom save(ParticipationRoom participationRoom);
}
