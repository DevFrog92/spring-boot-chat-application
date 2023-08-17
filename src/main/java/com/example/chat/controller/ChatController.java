package com.example.chat.controller;

import com.example.chat.dto.ChatMessageDto;
import com.example.chat.dto.ChatMessageType;
import com.example.chat.service.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatController {
    private final RedisTemplate<String, Object> redisTemplate;
    private final JwtProvider jwtProvider;
    private final ChannelTopic channelTopic;

    @MessageMapping("/chat/message")
    public void message(ChatMessageDto messageDto,
                        @Header("token") String token) {

        String username = jwtProvider.getUserNameFromJwt(token);

        // todo: change username to nickname
        messageDto.setSender(username);

        if (ChatMessageType.JOIN.equals(messageDto.getType())) {
            messageDto.setSender("[Notice 📣]");
            messageDto.setMessage(username + " joined this chatroom.");
        }

        // publishing to redis pub/sub
        redisTemplate.convertAndSend(
                channelTopic.getTopic(),
                messageDto
        );
    }
}
