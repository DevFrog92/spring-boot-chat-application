package com.example.chat.domain.chatroom.service;

import com.example.chat.domain.chatroom.dto.chatroom.RoomDto;
import com.example.chat.domain.member.dto.MemberDto;

public interface BlackListService {
    void addBlackList(RoomDto room, MemberDto banMember);
    boolean isMemberInBlackList(MemberDto member, RoomDto room);
}
