package com.example.chat.domain.chatroom.controller.facade;

import com.example.chat.domain.chatroom.controller.dto.response.ChatRoomInfoResponseDto;
import com.example.chat.domain.chatroom.domain.ChatRoom;
import com.example.chat.domain.chatroom.dto.ChatRoomCreateDto;

import java.util.List;

public interface ChatRoomFacade {
    ChatRoomInfoResponseDto create(ChatRoomCreateDto chatRoomCreateDto);
    void enter(Long memberId, Long roomId);
    void leave(Long memberId, Long roomId);
    void ban(String memberName, Long roomId);
    void delete(Long memberId, Long roomId);
    void submitCode(Long memberId, Long roomId, String code);
    ChatRoomInfoResponseDto getById(Long roomId);
    List<ChatRoomInfoResponseDto> findAllRooms(String memberName);
    PermissionType checkPermission(Long roomId, Long memberId);
}
