package com.example.chat.domain.member.controller.facade;

import com.example.chat.domain.member.controller.response.MemberInfoResponse;

public interface MemberFacade {
    MemberInfoResponse getInfo(String name);
}
