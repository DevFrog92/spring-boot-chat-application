package com.example.chat.domain.chatroom.controller.facade;

public interface ChatMessageFacade {
    void join(Long memberId, Long roomId);
    void leave(Long memberId, Long roomId);
    void ban(String memberName, Long roomId);
}
