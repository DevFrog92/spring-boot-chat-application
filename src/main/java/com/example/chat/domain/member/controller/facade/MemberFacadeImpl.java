package com.example.chat.domain.member.controller.facade;

import com.example.chat.domain.common.service.port.TokenHolder;
import com.example.chat.domain.member.controller.response.MemberInfoResponse;
import com.example.chat.domain.member.domain.Member;
import com.example.chat.domain.member.service.port.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberFacadeImpl implements MemberFacade {
    private final MemberService memberService;
    private final TokenHolder tokenHolder;

    @Override
    public MemberInfoResponse getInfo(String name) {
        Member member = memberService.getByName(name);
        String token = tokenHolder.generateToken(name);

        return MemberInfoResponse.from(member, token);
    }
}
