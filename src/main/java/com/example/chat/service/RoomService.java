package com.example.chat.service;

import com.example.chat.domain.BlackList;
import com.example.chat.domain.Member;
import com.example.chat.domain.Room;
import com.example.chat.dto.*;
import com.example.chat.repository.BlackListRepository;
import com.example.chat.repository.MemberRepository;
import com.example.chat.repository.ParticipationRoomRepository;
import com.example.chat.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;
    private final ParticipationRoomRepository participationRoomRepository;
    private final ParticipationRoomService participationRoomService;
    private final BlackListRepository blackListRepository;
    private final ChatService chatService;

    public Room createRoom(Member member, String roomName) {
        Room newRoom = Room.builder()
                .isPrivate(true)
                .roomName(roomName)
                .maxPoolSize(100)
                .member(member)
                .participationNum(0)
                .secretCode("qwer1234")
                .build();

        roomRepository.save(newRoom);

        return newRoom;
    }

    @Transactional
    public void checkEntryExamination(ChatEnterDto enterDto) {
        Long roomId = enterDto.getRoomId();
        Long requestMemberId = enterDto.getLoginInfo().getId();
        Room findRoom = getRoomInfo(roomId);
        Member requestMember = memberRepository
                .findById(requestMemberId)
                .orElseThrow();
        boolean isBanMember = blackListRepository
                .findByRoomAndMember(findRoom, requestMember).isPresent();

        if (isBanMember) {
            ChatBanDto chatBanDto = new ChatBanDto();
            chatBanDto.setType(ChatMessageType.INVALID);
            chatBanDto.setRoomId(roomId);
            chatBanDto.setBanMemberName(requestMember.getName());
            chatBanDto.setBanMemberId(requestMemberId);

            chatService.sendBanMessage(chatBanDto);
            return;
        }

        if (findRoom.getIsPrivate()
                && !findRoom.getMember().getId().equals(requestMember.getId())
                && !participationRoomService
                .isPermitMember(requestMember, findRoom)) {
            ChatBanDto chatBanDto = new ChatBanDto();
            chatBanDto.setType(ChatMessageType.INVALID);
            chatBanDto.setRoomId(roomId);
            chatBanDto.setBanMemberName(requestMember.getName());
            chatBanDto.setBanMemberId(requestMemberId);

            chatService.sendBanMessage(chatBanDto);
            return;
        }

        joinRoom(findRoom, requestMember);
    }

    // todo refactoring
    @Transactional
    public void joinRoom(Room room, Member member) {
        if (participationRoomService
                .isFirstJoinChatRoom(member, room)) {
            return;
        }

        participationRoomService.join(member, room);
        increaseRoomCount(room);
        ChatMessageDto messageDto = ChatMessageDto.builder()
                .roomId(room.getId())
                .message(member.getName() + " joined this chatroom.")
                .sender("[Notice 📣]")
                .build();
        chatService.sendChatMessage(messageDto);
    }

    @Transactional
    public void increaseRoomCount(Room room) {
        Room room1 = roomRepository.findById(room.getId()).orElseThrow();
        room1.setParticipationNum(room1.getParticipationNum()+1);
        log.info("update room cnt:{}", room1.getParticipationNum());

        chatService.sendUpdateChatRoomInfo(new ChatRoomInfoDto(room.getId(), room.getParticipationNum()));
    }


    @Transactional
    public void decreaseRoomCount(Room room) {
        room.setParticipationNum(room.getParticipationNum()-1);
        chatService.sendUpdateChatRoomInfo(new ChatRoomInfoDto(room.getId(), room.getParticipationNum()));
    }

    @Transactional
    public void leaveRoom(ChatEnterDto enterDto) {
        Room room = roomRepository.findById(enterDto.getRoomId()).orElseThrow();
        Member member = memberRepository.findById(enterDto.getLoginInfo().getId()).orElseThrow();
        participationRoomRepository.deleteByRoomAndMember(room, member);
        decreaseRoomCount(room);
        ChatMessageDto messageDto = ChatMessageDto.builder()
                .roomId(room.getId())
                .message(member.getName() + " left this chatroom.")
                .sender("[Notice 📣]")
                .build();
        chatService.sendChatMessage(messageDto);
    }

    public Room getRoomInfo(Long roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(() ->
                        new NoSuchElementException("존재하지 않는 방입니다."));
    }

    public List<Room> findAllRooms(String memberName) {
        Member member = memberRepository.findByName(memberName).orElseThrow();
        return roomRepository.findRoomsNotInBlacklistForUser(member.getId());
    }

    public void banMemberFromChatRoom(ChatBanDto banDto) {
        Room room = roomRepository.findById(banDto.getRoomId()).orElseThrow();
        Member banMember = memberRepository.findByName(banDto.getBanMemberName()).orElseThrow();

        banDto.setBanMemberId(banMember.getId());

        BlackList blackList = BlackList.builder()
                .room(room)
                .member(banMember)
                .build();

        blackListRepository.save(blackList);
        chatService.sendBanMessage(banDto);
    }

    public boolean checkSubmitSecretKey(ChatKeyDto dto) {
        Room room = roomRepository.findById(dto.getRoomId()).orElseThrow();
        Member member = memberRepository.findById(dto.getMemberId()).orElseThrow();

        if (participationRoomService.isFirstJoinChatRoom(member, room)) {
            return true;
        }


        if (room.getMember().equals(member) ||
                room.getSecretCode().equals(dto.getPassword())) {
            participationRoomService.submitSecretKey(member, room);
            return true;
        }

        return false;
    }

    public void deleteRoom(ChatDeleteDto dto) {
        Room room = roomRepository.findById(dto.getRoomId()).orElseThrow();

        if (!room.getMember().getId().equals(dto.getMemberId())) {
            log.info("방장만 삭제할 수 있습니다.");
            return;
        }

        roomRepository.deleteById(dto.getRoomId());
        chatService.sendDeletedChatRoom(dto);
    }

    public Room findById(Long roomId) {
        return roomRepository.findById(roomId).orElseThrow();
    }
}
