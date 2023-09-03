package com.example.chat.domain.chatroom.service.port;

import com.example.chat.domain.chatroom.domain.BlackList;
import com.example.chat.domain.chatroom.domain.ChatRoom;
import com.example.chat.domain.member.domain.Member;

import java.util.Optional;

public interface BlackListRepository {
    BlackList save(BlackList blackList);
    Optional<BlackList> findByRoomAndMember(ChatRoom room, Member member);
}
