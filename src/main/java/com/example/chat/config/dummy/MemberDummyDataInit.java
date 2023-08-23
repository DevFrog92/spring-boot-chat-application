package com.example.chat.config.dummy;

import com.example.chat.domain.Member;
import com.example.chat.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@Slf4j
@RequiredArgsConstructor
public class MemberDummyDataInit {
    private final MemberRepository userRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void initData() {
        log.info("dummy data init");
        userRepository.save(new Member("admin", "admin"));
        userRepository.save(new Member("guest", "guest"));
    }
}
