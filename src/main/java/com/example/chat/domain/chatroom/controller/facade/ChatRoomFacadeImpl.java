package com.example.chat.domain.chatroom.controller.facade;

import com.example.chat.domain.chatroom.domain.ChatRoom;
import com.example.chat.domain.chatroom.domain.ChatRoomCreate;
import com.example.chat.domain.chatroom.domain.ChatRoomType;
import com.example.chat.domain.chatroom.service.port.BlackListService;
import com.example.chat.domain.chatroom.service.port.ChatRoomService;
import com.example.chat.domain.chatroom.service.port.ParticipationRoomService;
import com.example.chat.domain.member.domain.Member;
import com.example.chat.domain.member.service.port.MemberService;
import com.example.chat.global.web.Exception.CustomIncorrectPassword;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.chat.domain.chatroom.controller.facade.PermissionType.*;

@Component
@RequiredArgsConstructor
public class ChatRoomFacadeImpl implements ChatRoomFacade {
    private final MemberService memberService;
    private final ChatRoomService chatRoomService;
    private final BlackListService blackListService;
    private final ParticipationRoomService participationRoomService;

    @Override
    @Transactional
    public ChatRoom create(ChatRoomCreate chatRoomCreate) {
        Member member = memberService.getById(chatRoomCreate.getRequestMemberId());
        return chatRoomService.create(member, chatRoomCreate);
    }

    @Override
    @Transactional
    public void delete(Long memberId, Long roomId) {
        Member member = memberService.getById(memberId);
        ChatRoom chatRoom = chatRoomService.getById(roomId);

        participationRoomService.deleteAllByRoom(chatRoom);
        chatRoomService.deleteRoom(member, chatRoom);
    }

    @Override
    @Transactional
    public void submitCode(Long memberId, Long roomId, String code) {
        Member member = memberService.getById(memberId);
        ChatRoom chatroom = chatRoomService.getById(roomId);

        if (!chatroom.checkCode(code)) {
            throw new CustomIncorrectPassword("비밀번호가 틀렸습니다.");
        }

        participationRoomService.submitSecretKey(member, chatroom, code);
    }

    @Override
    public ChatRoom getInfo(Long roomId) {
        return chatRoomService.getInfo(roomId);
    }

    @Override
    public List<ChatRoom> findAllRooms(String memberName) {
        Member member = memberService.getByName(memberName);
        return chatRoomService.findAllRooms(member);
    }

    @Override
    @Transactional
    public PermissionType checkPermission(Long roomId, Long memberId) {
        ChatRoom chatRoom = chatRoomService.getById(roomId);
        Member requestMember = memberService.getById(memberId);

        if (blackListService.isMemberInBlackList(requestMember, chatRoom)) {
            return NOT_ALLOW;
        }

        if (!participationRoomService.isCertifiedMember(requestMember, chatRoom)) {
            return NEED_PASSWORD;
        }

        return ALLOW;
    }
}
