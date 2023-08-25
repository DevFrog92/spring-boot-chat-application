package com.example.chat.domain.chatroom.service;

import com.example.chat.domain.chatroom.domain.Room;
import com.example.chat.domain.chatroom.domain.RoomRepository;
import com.example.chat.domain.chatroom.dto.chatroom.CreateRoomDto;
import com.example.chat.domain.chatroom.dto.chatroom.RoomDto;
import com.example.chat.domain.chatroom.dto.chatroom.RoomInfoDto;
import com.example.chat.domain.chatroom.dto.chatroom.SubmitSecretKeyDto;
import com.example.chat.domain.chatroom.dto.message.ChatNoticeDto;
import com.example.chat.domain.member.dto.BanMemberDto;
import com.example.chat.domain.member.dto.MemberDto;
import com.example.chat.domain.member.service.MemberService;
import com.example.chat.global.web.Exception.CustomNoSuchElementException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.chat.domain.chatroom.dto.message.ChatMessageType.BAN;
import static com.example.chat.domain.chatroom.dto.message.ChatMessageType.INVALID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {
    private final ChatService chatService;
    private final MemberService memberService;
    private final RoomRepository roomRepository;
    private final BlackListService blackListService;
    private final ParticipationRoomService participationRoomService;

    @Override
    @Transactional
    public RoomInfoDto createPublicRoom(CreateRoomDto dto) {
        RoomDto roomDto = RoomDto.builder()
                .isPrivate(false)
                .name(dto.getRoomName())
                .maxChatRoomSize(100)
                .participationNum(1)
                .build();

        return saveRoom(roomDto, dto.getRequestMemberId());
    }

    @Override
    @Transactional
    public RoomInfoDto createPrivateRoom(CreateRoomDto dto) {
        RoomDto roomDto = RoomDto.builder()
                .isPrivate(true)
                .name(dto.getRoomName())
                .maxChatRoomSize(100)
                .participationNum(1)
                .secretCode(dto.getSecretKey())
                .build();

        return saveRoom(roomDto, dto.getRequestMemberId());
    }

    @Transactional
    public RoomInfoDto saveRoom(RoomDto roomDto, Long memberId) {
        MemberDto memberDto = memberService.findMemberById(memberId);
        roomDto.setMember(memberDto.getEntity());

        Room savedRoom = roomRepository.save(roomDto.getEntity());

        participationRoomService.createParticipationRelation(
                memberDto,
                RoomDto.fromEntity(savedRoom));

        return RoomInfoDto.fromEntity(savedRoom);
    }

    @Override
    @Transactional
    public void joinRoom(ChatNoticeDto dto) {
        MemberDto member = memberService.findMemberById(dto.getParticipationId());
        RoomDto room = roomRepository.findById(dto.getRoomId()).stream()
                .map(RoomDto::fromEntity)
                .findFirst()
                .orElseThrow(() ->
                        new CustomNoSuchElementException("Room does not exist"));

        if (participationRoomService.alreadyJoinChatRoom(member, room)) {
            return;
        }

        if (room.getIsPrivate()) {
            participationRoomService.join(member, room);
        } else {
            participationRoomService.createParticipationRelation(member, room);
        }

        chatService.sendEnterChatRoomMessage(room.getId(), member.getName());
        increaseRoomCount(room);
    }

    @Override
    @Transactional
    public void increaseRoomCount(RoomDto room) {
        Room findRoom = roomRepository.findById(room.getId())
                .orElseThrow(() ->
                        new CustomNoSuchElementException("Room does not exist"));

        findRoom.setParticipationNum(findRoom.getParticipationNum() + 1);
        sendChatRoomInfoMessage(RoomDto.fromEntity(findRoom));
    }

    @Override
    @Transactional
    public void decreaseRoomCount(RoomDto room) {
        Room findRoom = roomRepository.findById(room.getId())
                .orElseThrow(() ->
                        new CustomNoSuchElementException("Room does not exist"));

        findRoom.setParticipationNum(Math.max(findRoom.getParticipationNum() - 1, 0));
        sendChatRoomInfoMessage(RoomDto.fromEntity(findRoom));
    }

    private void sendChatRoomInfoMessage(RoomDto room) {
        chatService.sendChatRoomInfo(room.getId(), room.getParticipationNum());
    }

    @Override
    @Transactional
    public void leaveRoom(ChatNoticeDto enterDto) {
        MemberDto member = memberService
                .findMemberById(enterDto.getParticipationId());
        Room room = roomRepository
                .findById(enterDto.getRoomId())
                .orElseThrow(() ->
                        new CustomNoSuchElementException("Room does not exist"));

        participationRoomService.deleteParticipationRelationByRoomAndMember(RoomDto.fromEntity(room), member);
        chatService.sendLeaveChatRoomMessage(room.getId(), member.getName());
        decreaseRoomCount(RoomDto.fromEntity(room));
    }

    @Override
    public RoomInfoDto getRoomInfo(Long roomId) {
        return roomRepository.findById(roomId)
                .stream()
                .map(RoomInfoDto::fromEntity)
                .findFirst()
                .orElseThrow(() ->
                        new CustomNoSuchElementException("Room does not exist"));
    }

    @Override
    public List<RoomInfoDto> findAllRooms(String memberName) {
        MemberDto member = memberService.findMemberByName(memberName);
        return roomRepository.
                findRoomsNotInBlacklistForUser(member.getId())
                .stream()
                .map(RoomInfoDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void banMemberFromChatRoom(BanMemberDto dto) {
        Room room = roomRepository
                .findById(dto.getRoomId())
                .orElseThrow(() ->
                        new CustomNoSuchElementException("Room does not exist"));

        if (isNotRoomOwner(dto.getRequestMemberId(), RoomDto.fromEntity(room))) {
            return;
        }

        MemberDto banMember = memberService.findMemberByName(dto.getBanMemberName());
        blackListService.addBlackList(RoomDto.fromEntity(room), banMember);
        chatService.sendMessageToMemberTopic(banMember.getId(), BAN);
    }

    private static boolean isNotRoomOwner(Long requestMemberId, RoomDto room) {
        return !room.getMember().getId().equals(requestMemberId);
    }

    @Override
    @Transactional
    public boolean checkSubmitSecretKey(SubmitSecretKeyDto dto) {
        MemberDto member = memberService.findMemberById(dto.getMemberId());
        Room room = roomRepository.findById(dto.getRoomId())
                .orElseThrow(() ->
                        new CustomNoSuchElementException("Room does not exist"));

        if (!room.getSecretCode().equals(dto.getPassword())) {
            return false;
        }

        participationRoomService.submitSecretKey(member, RoomDto.fromEntity(room));
        return true;
    }

    @Override
    @Transactional
    public void deleteRoom(ChatNoticeDto dto) {
        Room room = roomRepository.findById(dto.getRoomId())
                .orElseThrow(() ->
                        new CustomNoSuchElementException("Room does not exist"));

        if (!room.getMember().getId().equals(dto.getParticipationId())) {
            return;
        }

        roomRepository.deleteById(room.getId());
        chatService.sendChatRoomDeleted(room.getId());
        participationRoomService
                .deleteAllParticipationRelationByRoom(RoomDto.fromEntity(room));
    }

    @Override
    @Transactional
    public boolean isPermitMemberEnterChatRoom(Long roomId, Long memberId) {
        MemberDto member = memberService.findMemberById(memberId);
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() ->
                        new CustomNoSuchElementException("Room does not exist"));

        // check is room PUBLIC
        if (!room.getIsPrivate()) {
            return true;
        }

        // check request member is in black list
        if (blackListService.isMemberInBlackList(
                member, RoomDto.fromEntity(room))) {
            chatService.sendMessageToMemberTopic(member.getId(), INVALID);
            return false;
        }

        // check request member is already permitted room
        return participationRoomService.isPermitMember(
                member,
                RoomDto.fromEntity(room)
        );
    }
}
