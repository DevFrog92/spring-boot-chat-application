package com.example.chat.domain.chatroom.controller.facade;

import com.example.chat.domain.chatroom.domain.ChatRoom;
import com.example.chat.domain.chatroom.dto.ChatRoomCreateDto;

import java.util.List;

public interface ChatRoomFacade {
    ChatRoom create(ChatRoomCreateDto chatRoomCreateDto);
    void enter(Long memberId, Long roomId);
    void leave(Long memberId, Long roomId);
    void ban(String memberName, Long roomId);
    void delete(Long memberId, Long roomId);
    void submitCode(Long memberId, Long roomId, String code);
    ChatRoom getById(Long roomId);
    List<ChatRoom> findAllRooms(String memberName);
    PermissionType checkPermission(Long roomId, Long memberId);
}
