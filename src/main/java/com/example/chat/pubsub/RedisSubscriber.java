package com.example.chat.pubsub;

import com.example.chat.dto.ChatMessageDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisSubscriber implements MessageListener {
    private final ObjectMapper om;
    private final RedisTemplate<String, Object> redisTemplate;
    private final SimpMessageSendingOperations messagingTempalte;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String publishMessage = (String) redisTemplate.getStringSerializer()
                    .deserialize(message.getBody());

            ChatMessageDto chatMessage = om.readValue(publishMessage, ChatMessageDto.class);
            messagingTempalte.convertAndSend(
                    "/sub/chat/room/" + chatMessage.getRoomId(),
                    chatMessage);

        }catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
