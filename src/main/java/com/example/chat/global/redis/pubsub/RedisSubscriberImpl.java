package com.example.chat.global.redis.pubsub;

import com.example.chat.domain.chatroom.dto.message.ChatMessageDto;
import com.example.chat.domain.chatroom.dto.message.ChatRoomInfoDto;
import com.example.chat.domain.chatroom.dto.message.MemberTopicMessageDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisSubscriberImpl implements RedisSubscribe {
    private final ObjectMapper om;
    private final SimpMessageSendingOperations messagingTemplate;

    // todo exception handle -> message push fail -> internal exception
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

    public void sendChatRoomInfo(String publishMessage) {
        try {
            ChatRoomInfoDto chatRoomInfoDto = om.readValue(publishMessage, ChatRoomInfoDto.class);
            messagingTemplate.convertAndSend(
                    "/sub/chat/room/" + chatRoomInfoDto.getRoomId(),
                    chatRoomInfoDto
            );
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public void sendMessageToMember(String publishMessage) {
        try {
            MemberTopicMessageDto chatMessage = om.readValue(
                    publishMessage,
                    MemberTopicMessageDto.class
            );

            messagingTemplate.convertAndSend(
                    "/sub/member/" + chatMessage.getMemberId(),
                    chatMessage);

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
