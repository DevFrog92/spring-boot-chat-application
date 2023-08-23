package com.example.chat.controller;

import com.example.chat.domain.Member;
import com.example.chat.domain.MemberInfo;
import com.example.chat.service.JwtProvider;
import com.example.chat.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final JwtProvider jwtProvider;

    @GetMapping
    public MemberInfo getMemberInfo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        Member member = memberService.findMemberByName(name);
        return MemberInfo.builder()
                .id(member.getId())
                .name(name)
                .token(jwtProvider.generateToken(name))
                .build();
    }
}
