package com.example.chat.domain.chatroom.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatRoomController {

    @GetMapping("/room")
    public String room() {
        return "/chat/room";
    }

    @GetMapping("/room/{roomId}")
    public String roomDetail() {
        return "/chat/roomDetail";
    }
}
