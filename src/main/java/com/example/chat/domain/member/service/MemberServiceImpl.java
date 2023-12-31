package com.example.chat.domain.member.service;

import com.example.chat.domain.member.domain.Member;
import com.example.chat.domain.member.service.port.MemberRepository;
import com.example.chat.domain.member.service.port.MemberService;
import com.example.chat.domain.common.Exception.CustomNoSuchElementException;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Builder
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;

    @Override
    public Member getByName(String name) {
        return memberRepository.getByName(name)
                .orElseThrow(CustomNoSuchElementException::new);
    }

    @Override
    public Member getById(Long id) {
        return memberRepository.getById(id)
                .orElseThrow(CustomNoSuchElementException::new);
    }
}
