package com.example.chat.domain.chatroom.service;

import com.example.chat.domain.chatroom.dto.chatroom.RoomDto;
import com.example.chat.domain.member.dto.MemberDto;

public interface ParticipationRoomService {
    void join(MemberDto member, RoomDto room);

    void createParticipationRelation(MemberDto member, RoomDto room);

    void submitSecretKey(MemberDto member, RoomDto room);

    boolean isPermitMember(MemberDto member, RoomDto room);

    boolean alreadyJoinChatRoom(MemberDto member, RoomDto room);

    void deleteParticipationRelationByRoom(RoomDto room);

    void deleteParticipationRelationByRoomAndMember(RoomDto room, MemberDto member);
}
