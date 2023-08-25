package com.example.chat.domain.member.controller;

import com.example.chat.global.web.dto.ResponseDto;
import com.example.chat.domain.member.dto.MemberInfoDto;
import com.example.chat.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<?> getMemberInfo(Authentication auth) {
        MemberInfoDto memberInfo = memberService.getMemberInfoWithToken(auth.getName());
        return new ResponseEntity<>(
                new ResponseDto<>(
                        "회원 조회를 완료했습니다.",
                        memberInfo
                ),
                HttpStatus.OK
        );
    }
}
