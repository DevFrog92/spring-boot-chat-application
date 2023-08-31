package com.example.chat.global.config.redis;

import com.example.chat.domain.common.service.port.RedisSubscribe;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisMessageListenerContainer redisMessageListener(
            RedisConnectionFactory connectionFactory,
            MessageListenerAdapter listenerAdapterChatRoom,
            MessageListenerAdapter listenerAdapterForMember,
            MessageListenerAdapter listenerAdapterChatRoomInfo
    ) {

        RedisMessageListenerContainer container
                = new RedisMessageListenerContainer();

        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listenerAdapterChatRoom, new ChannelTopic("chatroom"));
        container.addMessageListener(listenerAdapterForMember, new ChannelTopic("member"));
        container.addMessageListener(listenerAdapterChatRoomInfo, new ChannelTopic("chatroomInfo"));

        return container;
    }

    @Bean
    public MessageListenerAdapter listenerAdapterChatRoom(RedisSubscribe subscriber) {
        return new MessageListenerAdapter(
                subscriber,
                "sendMessage");
    }

    @Bean
    public MessageListenerAdapter listenerAdapterForMember(RedisSubscribe subscriber) {
        return new MessageListenerAdapter(
                subscriber,
                "sendMessageToMember");
    }

    @Bean
    public MessageListenerAdapter listenerAdapterChatRoomInfo(RedisSubscribe subscriber) {
        return new MessageListenerAdapter(
                subscriber,
                "sendChatRoomInfo");
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(
            RedisConnectionFactory connectionFactory
    ) {

        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(String.class));

        return redisTemplate;
    }
}
