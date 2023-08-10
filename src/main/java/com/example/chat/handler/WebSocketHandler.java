package com.example.chat.handler;

import com.example.chat.domain.ChatRoom;
import com.example.chat.dto.ChatMessageDto;
import com.example.chat.service.ChatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {
    private final ObjectMapper objectMapper;
    private final ChatService chatService;

    @Override
    protected void handleTextMessage(
            WebSocketSession session,
            TextMessage message) throws Exception {
        String payload = message.getPayload();

        log.info("payload: {}", payload);

        ChatMessageDto chatMessageDto = objectMapper.readValue(payload, ChatMessageDto.class);
        ChatRoom room = chatService.findRoomById(chatMessageDto.getRoomId());

        room.handleActions(session, chatMessageDto, chatService);
    }
}
