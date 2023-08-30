package com.example.chat.domain.chatroom.controller.facade;

import com.example.chat.domain.chatroom.domain.ChatRoom;
import com.example.chat.domain.chatroom.service.port.BlackListService;
import com.example.chat.domain.chatroom.service.port.ChatRoomService;
import com.example.chat.domain.chatroom.service.port.ParticipationRoomService;
import com.example.chat.domain.member.domain.Member;
import com.example.chat.domain.member.service.port.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ChatMessageFacadeImpl implements ChatMessageFacade {
    private final MemberService memberService;
    private final ChatRoomService chatRoomService;
    private final ParticipationRoomService participationRoomService;
    private final BlackListService blackListService;

    @Override
    @Transactional
    public void join(Long memberId, Long roomId) {
        Member member = memberService.getById(memberId);
        ChatRoom chatRoom = chatRoomService.getById(roomId);

        if(participationRoomService.isCertifiedMember(member, chatRoom)) {
           return;
        }

        chatRoomService.updateParticipationNum(chatRoom.join());
        participationRoomService.create(member, chatRoom);
    }

    @Override
    @Transactional
    public void leave(Long memberId, Long roomId) {
        Member member = memberService.getById(memberId);
        ChatRoom chatRoom = chatRoomService.getById(roomId);

        chatRoomService.updateParticipationNum(chatRoom.leave());
        participationRoomService.deleteByMemberAndRoom(member, chatRoom);
    }

    @Override
    @Transactional
    public void ban(String memberName, Long roomId) {
        Member member = memberService.getByName(memberName);
        ChatRoom chatRoom = chatRoomService.getById(roomId);

        blackListService.create(chatRoom, member);
    }
}
