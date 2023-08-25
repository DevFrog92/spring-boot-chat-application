package com.example.chat.domain.chatroom.controller;

import com.example.chat.domain.member.dto.BanMemberDto;
import com.example.chat.domain.chatroom.dto.message.ChatMessageDto;
import com.example.chat.domain.chatroom.dto.message.ChatNoticeDto;
import com.example.chat.domain.chatroom.service.ChatRoomService;
import com.example.chat.domain.chatroom.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;
    private final ChatRoomService chatRoomService;

    @MessageMapping("/chat/message")
    public void message(@RequestBody ChatMessageDto messageDto) {
        chatService.sendChatMessage(messageDto);
    }

    @MessageMapping("/chat/enter")
    public void enter(@RequestBody ChatNoticeDto enterDto) {
        chatRoomService.checkEntryExamination(enterDto);
    }

    @MessageMapping("/chat/leave")
    public void leave(@RequestBody ChatNoticeDto enterDto) {
        chatRoomService.leaveRoom(enterDto);
    }

    @MessageMapping("/chat/ban")
    public void ban(@RequestBody BanMemberDto banDto) {
        chatRoomService.banMemberFromChatRoom(banDto);
    }
}
