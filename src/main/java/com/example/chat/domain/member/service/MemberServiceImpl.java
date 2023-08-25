package com.example.chat.domain.member.service;

import com.example.chat.domain.member.domain.Member;
import com.example.chat.global.web.Exception.CustomNoSuchElementException;
import com.example.chat.domain.member.dto.MemberDto;
import com.example.chat.domain.member.dto.MemberInfoDto;
import com.example.chat.domain.member.domain.MemberRepository;
import com.example.chat.global.jwt.provider.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final JwtProvider jwtService;

    @Override
    public MemberDto findMemberByName(String name) {
        Member member = memberRepository.findByName(name)
                .orElseThrow(() ->
                        new CustomNoSuchElementException("Member not exist"));
        return MemberDto.fromEntity(member);
    }

    @Override
    public MemberDto findMemberById(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() ->
                        new CustomNoSuchElementException("Member not exist"));
        return MemberDto.fromEntity(member);
    }


    @Override
    public MemberInfoDto getMemberInfoWithToken(String memberName) {
        MemberDto member = findMemberByName(memberName);
        return MemberInfoDto.builder()
                .id(member.getId())
                .name(member.getName())
                .nickname(member.getNickname())
                .token(jwtService.generateToken(member.getName()))
                .build();
    }
}
