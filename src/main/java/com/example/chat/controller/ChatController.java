package com.example.chat.controller;

import com.example.chat.dto.ChatMessageDto;
import com.example.chat.dto.ChatMessageType;
import com.example.chat.pubsub.RedisPublisher;
import com.example.chat.repository.ChatRoomRepository;
import com.example.chat.service.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;

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
        // 로그인 회원 정보로 대화면을 설정 -> 닉네임으로 바꿀 수 있도록 할 것인가?

        messageDto.setSender(username);

        if (ChatMessageType.JOIN.equals(messageDto.getType())) {
            messageDto.setSender("[알림]");
            messageDto.setMessage(username + "님이 입장하셨습니다.");
        }

        // Websocket 에 발행된 메시지를 redis 로 발행 (publish)
        redisTemplate.convertAndSend(channelTopic.getTopic(), messageDto);
    }
}
