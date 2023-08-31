package com.example.chat.domain.chatroom.controller.facade;

import com.example.chat.domain.chatroom.domain.ChatRoom;
import com.example.chat.domain.chatroom.domain.ChatRoomCreate;

import java.util.List;

public interface ChatRoomFacade {
    ChatRoom create(ChatRoomCreate chatRoomCreate);
    void enter(Long memberId, Long roomId);
    void leave(Long memberId, Long roomId);
    void ban(String memberName, Long roomId);
    void delete(Long memberId, Long roomId);
    void submitCode(Long memberId, Long roomId, String code);
    ChatRoom getInfo(Long roomId);
    List<ChatRoom> findAllRooms(String memberName);
    PermissionType checkPermission(Long roomId, Long memberId);
}
