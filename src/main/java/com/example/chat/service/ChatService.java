package com.example.chat.service;

import com.example.chat.domain.ChatRoom;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatService {

    private final ObjectMapper objectMapper;
    private Map<String, ChatRoom> chatRooms;

    @PostConstruct
    private void init() {
        chatRooms = new LinkedHashMap<>();
    }

    public List<ChatRoom> findAllRoom() {
        return new ArrayList<>(chatRooms.values());
    }

    public ChatRoom findRoomById(String roomId) {
        return chatRooms.get(roomId);
    }

    public ChatRoom createRoom(String name) {
        String roomId = UUID.randomUUID().toString();
        ChatRoom newRoom = ChatRoom.builder()
                .roomId(roomId)
                .name(name)
                .build();
        log.info("create new room: {}", newRoom);
        chatRooms.put(roomId, newRoom);
        return newRoom;
    }

    public <T> void sendMessage(WebSocketSession session,
                                T message) {
        try {
            session.sendMessage(new TextMessage(objectMapper.writeValueAsBytes(message)));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
