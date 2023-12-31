package com.example.chat;

import com.example.chat.domain.member.infrastructure.MemberJpaRepository;
import com.example.chat.global.dummymember.MemberDummyDataInit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatApplication.class, args);
    }

    @Bean
    @Profile("test")
    public MemberDummyDataInit memberTestDataInit(MemberJpaRepository userRepository) {
        return new MemberDummyDataInit(userRepository);
    }
}
