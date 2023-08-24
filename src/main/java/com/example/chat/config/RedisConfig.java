package com.example.chat.config;

import com.example.chat.pubsub.RedisSubscriber;
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

    // set topic to single
    @Bean
    public ChannelTopic channelTopic() {
        return new ChannelTopic("chatroom");
    }

    // setup link stomp websocket with redis message broker
    @Bean
    public RedisMessageListenerContainer redisMessageListener(
            RedisConnectionFactory connectionFactory,
            MessageListenerAdapter listenerAdapterChatRoom,
            MessageListenerAdapter listenerAdapterBan,
            MessageListenerAdapter listenerAdapterDeletedChatRoom,
            MessageListenerAdapter listenerAdapterChatRoomInfo
    ) {

        RedisMessageListenerContainer container
                = new RedisMessageListenerContainer();

        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listenerAdapterChatRoom, new ChannelTopic("chatroom"));
        container.addMessageListener(listenerAdapterBan, new ChannelTopic("banMember"));
        container.addMessageListener(listenerAdapterDeletedChatRoom, new ChannelTopic("deleteChatRoom"));
        container.addMessageListener(listenerAdapterChatRoomInfo, new ChannelTopic("updateChatRoomInfo"));

        return container;
    }


    // setup stomp message broker action when get the push from redis
    @Bean
    public MessageListenerAdapter listenerAdapterChatRoom(RedisSubscriber subscriber) {
        return new MessageListenerAdapter(
                subscriber,
                "sendMessage");
    }

    @Bean
    public MessageListenerAdapter listenerAdapterBan(RedisSubscriber subscriber) {
        return new MessageListenerAdapter(
                subscriber,
                "sendBanMessage");
    }

    @Bean
    public MessageListenerAdapter listenerAdapterDeletedChatRoom(RedisSubscriber subscriber) {
        return new MessageListenerAdapter(
                subscriber,
                "sendDeletedChatRoom");
    }

    @Bean
    public MessageListenerAdapter listenerAdapterChatRoomInfo(RedisSubscriber subscriber) {
        return new MessageListenerAdapter(
                subscriber,
                "sendChatRoomInfo");
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(
            RedisConnectionFactory connectionFactory
    ) {

        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

        // connect with redis database
        redisTemplate.setConnectionFactory(connectionFactory);
        // setup serializer method
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        // setup value serializer method
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(String.class));

        return redisTemplate;
    }
}
