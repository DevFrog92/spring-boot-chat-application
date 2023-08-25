package com.example.chat.domain.member.service;

import com.example.chat.domain.member.dto.MemberDto;
import com.example.chat.domain.member.dto.MemberInfoDto;

public interface MemberService {
    MemberDto findMemberByName(String name);
    MemberDto findMemberById(Long id);
    MemberInfoDto getMemberInfoWithToken(String memberName);
}
