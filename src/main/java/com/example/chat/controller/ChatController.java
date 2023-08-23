package com.example.chat.controller;

import com.example.chat.dto.ChatBanDto;
import com.example.chat.dto.ChatEnterDto;
import com.example.chat.dto.ChatMessageDto;
import com.example.chat.service.ChatService;
import com.example.chat.service.JwtProvider;
import com.example.chat.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatController {
    private final JwtProvider jwtProvider;
    private final ChatService chatService;
    private final RoomService roomService;

    @MessageMapping("/chat/message")
    public void message(@RequestBody ChatMessageDto messageDto,
                        @Header("token") String token) {

        String username = jwtProvider.getUserNameFromJwt(token);
        messageDto.setSender(username);
        chatService.sendChatMessage(messageDto);
    }

    @MessageMapping("/chat/enter")
    public void enter(@RequestBody ChatEnterDto enterDto) {
        roomService.checkEntryExamination(enterDto);
    }

    @MessageMapping("/chat/leave")
    public void leave(@RequestBody ChatEnterDto enterDto) {
        roomService.leaveRoom(enterDto);
    }

    @MessageMapping("/chat/ban")
    public void ban(@RequestBody ChatBanDto banDto) {
        roomService.banMemberFromChatRoom(banDto);
    }
}
