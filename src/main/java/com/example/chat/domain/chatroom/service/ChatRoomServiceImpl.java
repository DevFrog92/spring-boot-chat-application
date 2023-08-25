package com.example.chat.domain.chatroom.service;

import com.example.chat.domain.chatroom.domain.BlackList;
import com.example.chat.domain.chatroom.dto.chatroom.RoomDto;
import com.example.chat.domain.chatroom.dto.chatroom.RoomInfoDto;
import com.example.chat.domain.chatroom.dto.chatroom.SubmitSecretKeyDto;
import com.example.chat.domain.chatroom.dto.message.ChatNoticeDto;
import com.example.chat.domain.member.domain.Member;
import com.example.chat.domain.chatroom.domain.Room;
import com.example.chat.domain.member.dto.BanMemberDto;
import com.example.chat.domain.member.service.MemberService;
import com.example.chat.global.web.Exception.CustomNoSuchElementException;
import com.example.chat.domain.member.dto.MemberDto;
import com.example.chat.domain.chatroom.domain.BlackListRepository;
import com.example.chat.domain.chatroom.domain.RoomRepository;
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
    private final BlackListRepository blackListRepository;
    private final ParticipationRoomService participationRoomService;

    @Override
    @Transactional
    public RoomInfoDto createRoom(Long memberId, String roomName) {
        MemberDto memberDto = memberService.findMemberById(memberId);
        Member member = memberDto.getEntity();
        Room newRoom = roomRepository.save(Room.builder()
                .isPrivate(true)
                .name(roomName)
                .maxPoolSize(100)
                .member(member)
                .participationNum(1)
                .secretCode("qwer1234")
                .build());

        participationRoomService.createParticipationRelation(
                memberDto,
                RoomDto.fromEntity(newRoom));

        return RoomInfoDto.fromEntity(newRoom);
    }

    @Override
    @Transactional
    public void checkEntryExamination(ChatNoticeDto enterDto) {
        Long roomId = enterDto.getRoomId();
        Long requestMemberId = enterDto.getParticipationId();
        MemberDto requestMember = memberService
                .findMemberById(requestMemberId);
        Room findRoom = roomRepository
                .findById(roomId)
                .orElseThrow(() ->
                        new CustomNoSuchElementException("Room does not exist"));

        if (isMemberInBlackList(requestMember, RoomDto.fromEntity(findRoom))) {
            chatService.sendMessageToMemberTopic(requestMemberId, INVALID);
            return;
        }

        joinRoom(RoomDto.fromEntity(findRoom), requestMember);
    }

    @Override
    public boolean isMemberInBlackList(MemberDto member, RoomDto room) {
        return blackListRepository
                .findByRoomAndMember(
                        room.getEntity(),
                        member.getEntity())
                .isPresent();
    }


    @Override
    @Transactional
    public void joinRoom(RoomDto room, MemberDto member) {
        if (participationRoomService.alreadyJoinChatRoom(member, room)) {
            return;
        }

        participationRoomService.join(member, room);
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

        if (!room.getMember().getId().equals(dto.getRequestMemberId())) {
            return;
        }

        MemberDto banMember = memberService.findMemberByName(dto.getBanMemberName());

        // todo separate to blacklist service
        BlackList blackList = BlackList.builder()
                .room(room)
                .member(banMember.getEntity())
                .build();

        blackListRepository.save(blackList);
        chatService.sendMessageToMemberTopic(banMember.getId(), BAN);
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
                .deleteParticipationRelationByRoom(RoomDto.fromEntity(room));
    }

    @Override
    @Transactional
    public boolean isPermitMemberEnterChatRoom(Long roomId, Long memberId) {
        MemberDto member = memberService.findMemberById(memberId);
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() ->
                        new CustomNoSuchElementException("Room does not exist"));

        if (!room.getIsPrivate()) {
            return true;
        }

        return participationRoomService.isPermitMember(
                member,
                RoomDto.fromEntity(room)
        );
    }
}
