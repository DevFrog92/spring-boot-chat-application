package com.example.chat.controller;

import com.example.chat.dto.ChatMessageDto;
import com.example.chat.dto.ChatMessageType;
import com.example.chat.pubsub.RedisPublisher;
import com.example.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatController {
    private final RedisPublisher redisPublisher;
    private final ChatRoomRepository chatRoomRepository;

    @MessageMapping("/chat/message")
    public void message(ChatMessageDto messageDto) {
        if (ChatMessageType.JOIN.equals(messageDto.getType())) {
            chatRoomRepository.enterChatRoom(messageDto.getRoomId());
            messageDto.setMessage(messageDto.getSender() + "님이 입장하셨습니다.");
        }

        redisPublisher.publish(
                chatRoomRepository.getTopic(messageDto.getRoomId()),
                messageDto
        );
    }
}
