package com.example.chat.service;

import com.example.chat.domain.Member;
import com.example.chat.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberService;

    public Member findMemberByName(String name) {
        return memberService.findByName(name)
                .orElseThrow(() ->
                        new NoSuchElementException("Member not exist"));
    }

    // todo
    public Member getMemberInfo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        return findMemberByName(name);
    }

    public Member findMemberById(Long memberId) {
        return memberService.findById(memberId)
                .orElseThrow(() ->
                        new NoSuchElementException("Member not exist"));
    }
}
