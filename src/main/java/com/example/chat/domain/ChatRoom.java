package com.example.chat.domain;

import com.example.chat.dto.ChatMessageDto;
import com.example.chat.service.ChatService;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;

import static com.example.chat.dto.ChatMessageType.JOIN;

@Getter
public class ChatRoom {
    private final String roomId;
    private final String name;
    private final Set<WebSocketSession> sessions = new HashSet<>();

    @Builder
    public ChatRoom(String roomId, String name) {
        this.roomId = roomId;
        this.name = name;
    }

    public void handleActions(
            WebSocketSession session,
            ChatMessageDto chatMessageDto,
            ChatService chatService
    ) {
        if(chatMessageDto.getType().equals(JOIN)) {
            sessions.add(session);
            chatMessageDto
                    .setMessage(chatMessageDto.getSender() + "님이 입장하셨습니다.");
        }

        sendMessage(chatMessageDto, chatService);
    }

    public <T> void sendMessage(T message, ChatService chatService) {
        sessions.parallelStream()
                .forEach(session -> chatService.sendMessage(session, message));
    }
}