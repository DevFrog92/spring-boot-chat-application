package com.example.chat.domain.chatroom.service;

import com.example.chat.domain.chatroom.dto.chatroom.CreateRoomDto;
import com.example.chat.domain.chatroom.dto.chatroom.RoomDto;
import com.example.chat.domain.chatroom.dto.chatroom.RoomInfoDto;
import com.example.chat.domain.chatroom.dto.chatroom.SubmitSecretKeyDto;
import com.example.chat.domain.chatroom.dto.message.ChatNoticeDto;
import com.example.chat.domain.member.dto.BanMemberDto;

import java.util.List;

public interface ChatRoomService {
    RoomInfoDto createPublicRoom(CreateRoomDto dto);
    RoomInfoDto createPrivateRoom(CreateRoomDto dto);

    void joinRoom(ChatNoticeDto dto);

    void leaveRoom(ChatNoticeDto enterDto);

    void increaseRoomCount(RoomDto room);

    void decreaseRoomCount(RoomDto room);

    List<RoomInfoDto> findAllRooms(String memberName);

    void banMemberFromChatRoom(BanMemberDto banDto);

    boolean checkSubmitSecretKey(SubmitSecretKeyDto dto);

    void deleteRoom(ChatNoticeDto dto);

    RoomInfoDto getRoomInfo(Long roomId);

    boolean isPermitMemberEnterChatRoom(Long roomId, Long memberId);
}
