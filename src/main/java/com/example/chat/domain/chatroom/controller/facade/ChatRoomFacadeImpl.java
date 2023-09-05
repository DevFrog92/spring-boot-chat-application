package com.example.chat.domain.chatroom.controller.facade;

import com.example.chat.domain.chatroom.controller.dto.response.ChatRoomInfoResponseDto;
import com.example.chat.domain.chatroom.domain.ChatRoom;
import com.example.chat.domain.chatroom.domain.ParticipationRoom;
import com.example.chat.domain.chatroom.dto.ChatRoomCreateDto;
import com.example.chat.domain.chatroom.dto.message.ChatMessageType;
import com.example.chat.domain.chatroom.service.port.BlackListService;
import com.example.chat.domain.chatroom.service.port.ChatRoomService;
import com.example.chat.domain.chatroom.service.port.ChatService;
import com.example.chat.domain.chatroom.service.port.ParticipationRoomService;
import com.example.chat.domain.common.Exception.CustomIncorrectPassword;
import com.example.chat.domain.common.Exception.CustomNotAllowAccess;
import com.example.chat.domain.member.domain.Member;
import com.example.chat.domain.member.service.port.MemberService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.example.chat.domain.chatroom.controller.facade.PermissionType.*;
import static com.example.chat.domain.chatroom.domain.ChatRoomType.PRIVATE;

@Slf4j
@Component
@Builder
@RequiredArgsConstructor
public class ChatRoomFacadeImpl implements ChatRoomFacade {
    private final MemberService memberService;
    private final ChatService chatService;
    private final ChatRoomService chatRoomService;
    private final BlackListService blackListService;
    private final ParticipationRoomService participationRoomService;

    @Override
    @Transactional
    public ChatRoomInfoResponseDto create(ChatRoomCreateDto chatRoomCreateDto) {
        Member member = memberService
                .getById(chatRoomCreateDto.getRequestMemberId());

        return ChatRoomInfoResponseDto.from(
                chatRoomService.create(member, chatRoomCreateDto)
        );
    }

    @Override
    @Transactional
    public void enter(Long memberId, Long roomId) {
        Member member = memberService.getById(memberId);
        ChatRoom chatRoom = chatRoomService.getById(roomId);

        if (blackListService.isMemberInBlackList(member, chatRoom)) {
            throw new CustomNotAllowAccess();
        }

        // todo is possible to remove if statement?
        if (chatRoom.getType().equals(PRIVATE)) {
            enterPrivate(member, chatRoom);
        } else {
            enterPublic(member, chatRoom);
        }
    }

    @Transactional
    public void enterPublic(Member member, ChatRoom chatRoom) {
        if (participationRoomService.alreadyJoined(member, chatRoom)) {
            return;
        }

        participationRoomService.create(member, chatRoom);
        participationRoomService.join(member, chatRoom);
        chatRoomService.updateParticipationNum(chatRoom.join());
        chatService.sendEnterChatRoomMessage(chatRoom.getId(), member.getName());
        chatService.sendChatRoomInfo(chatRoom.getId(), chatRoom.join().getParticipationNum());
    }

    @Transactional
    public void enterPrivate(Member member, ChatRoom chatRoom) {
        ParticipationRoom participationRoom = participationRoomService
                .getByMemberAndRoom(member, chatRoom);

        if (participationRoom == null) {
            throw new CustomNotAllowAccess();
        }

        if (participationRoom.getJoined()) {
            return;
        }

        participationRoomService.join(member, chatRoom);
        chatRoomService.updateParticipationNum(chatRoom.join());
        chatService.sendEnterChatRoomMessage(chatRoom.getId(), member.getName());
        chatService.sendChatRoomInfo(chatRoom.getId(), chatRoom.join().getParticipationNum());
    }

    @Override
    @Transactional
    public void leave(Long memberId, Long roomId) {
        Member member = memberService.getById(memberId);
        ChatRoom chatRoom = chatRoomService.getById(roomId);

        chatRoomService.updateParticipationNum(chatRoom.leave());
        participationRoomService.deleteByMemberAndRoom(member, chatRoom);
        chatService.sendLeaveChatRoomMessage(chatRoom.getId(), member.getName());
        chatService.sendChatRoomInfo(chatRoom.getId(), chatRoom.leave().getParticipationNum());
    }

    @Override
    @Transactional
    public void ban(String memberName, Long roomId) {
        Member member = memberService.getByName(memberName);
        ChatRoom chatRoom = chatRoomService.getById(roomId);

        blackListService.create(chatRoom, member);
        chatService.sendMessageToMemberTopic(member.getId(), ChatMessageType.BAN);
    }

    @Override
    @Transactional
    public void delete(Long memberId, Long roomId) {
        Member member = memberService.getById(memberId);
        ChatRoom chatRoom = chatRoomService.getById(roomId);

        participationRoomService.deleteAllByRoom(chatRoom);
        chatRoomService.deleteRoom(member, chatRoom);
        chatService.sendChatRoomDeleted(chatRoom.getId());
    }

    @Override
    @Transactional
    public void submitCode(Long memberId, Long roomId, String code) {
        Member member = memberService.getById(memberId);
        ChatRoom chatroom = chatRoomService.getById(roomId);

        if (!chatroom.checkCode(code)) {
            throw new CustomIncorrectPassword();
        }

        participationRoomService.submitSecretKey(member, chatroom, code);
    }

    @Override
    public ChatRoomInfoResponseDto getById(Long roomId) {
        return ChatRoomInfoResponseDto.from(chatRoomService.getById(roomId));
    }

    @Override
    public List<ChatRoomInfoResponseDto> findAllRooms(String memberName) {
        Member member = memberService.getByName(memberName);

        return chatRoomService
                .findAllRooms(member)
                .stream()
                .map(ChatRoomInfoResponseDto::from)
                .collect(Collectors.toList());
    }

    // todo it is duplicate code to compare enter method?
    @Override
    @Transactional
    public PermissionType checkPermission(Long roomId, Long memberId) {
        ChatRoom chatRoom = chatRoomService.getById(roomId);
        Member requestMember = memberService.getById(memberId);

        if (blackListService.isMemberInBlackList(requestMember, chatRoom)) {
            return NOT_ALLOW;
        }

        if (Objects.equals(chatRoom.getType(), PRIVATE) &&
                !participationRoomService.isCertifiedMember(requestMember, chatRoom)) {
            return NEED_PASSWORD;
        }

        return ALLOW;
    }
}
