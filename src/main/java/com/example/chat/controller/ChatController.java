package com.example.chat.controller;

import com.example.chat.dto.ChatMessageDto;
import com.example.chat.dto.ChatMessageType;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatController {
    private final SimpMessageSendingOperations messagingTemplate;

    @MessageMapping("/chat/message")
    public void message(ChatMessageDto messageDto) {
        if (ChatMessageType.JOIN.equals(messageDto.getType())) {
            messageDto.setMessage(messageDto.getSender() + "님이 입장하셨습니다.");
        }

        messagingTemplate.convertAndSend(
                "/sub/chat/room/" + messageDto.getRoomId(),
                messageDto);
    }
}
