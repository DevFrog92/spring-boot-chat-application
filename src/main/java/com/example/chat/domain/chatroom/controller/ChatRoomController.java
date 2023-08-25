package com.example.chat.domain.chatroom.controller;

import com.example.chat.domain.member.dto.MemberDto;
import com.example.chat.domain.chatroom.service.ChatRoomService;
import com.example.chat.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatRoomController {
    private final ChatRoomService chatRoomService;
    private final MemberService memberService;

    @GetMapping("/room")
    public String room() {
        return "/chat/room";
    }

    @GetMapping("/room/{roomId}")
    public String roomDetail(@PathVariable Long roomId,
                             Authentication auth) {
        MemberDto memberInfo = memberService.findMemberByName(auth.getName());

        if (!chatRoomService.isPermitMemberEnterChatRoom(roomId, memberInfo.getId())) {
            return "redirect:/chat/room";
        }

        return "/chat/roomDetail";
    }
}
