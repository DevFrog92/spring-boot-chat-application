package com.example.chat.domain.chatroom.service.port;

import com.example.chat.domain.chatroom.domain.ChatRoom;
import com.example.chat.domain.member.domain.Member;

public interface BlackListService {
    void create(ChatRoom room, Member banMember);
    boolean isMemberInBlackList(Member member, ChatRoom room);
}
