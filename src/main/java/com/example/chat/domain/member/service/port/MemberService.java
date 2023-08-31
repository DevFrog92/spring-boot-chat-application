package com.example.chat.domain.member.service.port;

import com.example.chat.domain.member.domain.Member;

public interface MemberService {
    Member getByName(String name);
    Member getById(Long id);
}
