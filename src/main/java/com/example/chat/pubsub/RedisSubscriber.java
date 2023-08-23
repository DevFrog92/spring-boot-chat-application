package com.example.chat.pubsub;

import com.example.chat.dto.ChatBanDto;
import com.example.chat.dto.ChatDeleteDto;
import com.example.chat.dto.ChatMessageDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisSubscriber {
    private final ObjectMapper om;
    private final SimpMessageSendingOperations messagingTemplate;

    /**
     * Redis 에서 메시지가 발생 (public) 되면 대기하고
     * 있던 Redis Subscribe 가 해당 메시지를 받아 처리한다.
     */

    public void sendMessage(String publishMessage) {
        try {
            ChatMessageDto chatMessage = om.readValue(publishMessage, ChatMessageDto.class);
            messagingTemplate.convertAndSend(
                    "/sub/chat/room/" + chatMessage.getRoomId(),
                    chatMessage);

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public void sendBanMessage(String publishMessage) {
        try {
            ChatBanDto chatMessage = om.readValue(publishMessage, ChatBanDto.class);
            messagingTemplate.convertAndSend(
                    "/sub/member/"+chatMessage.getBanMemberId(),
                    chatMessage);

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public void sendDeletedChatRoom(String publishMessage) {
        try {
            ChatDeleteDto chatMessage = om.readValue(
                    publishMessage,
                    ChatDeleteDto.class
            );

            messagingTemplate.convertAndSend(
                    "/sub/chat/room/" + chatMessage.getRoomId(),
                    chatMessage);

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
