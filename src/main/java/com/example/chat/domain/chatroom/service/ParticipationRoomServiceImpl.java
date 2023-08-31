package com.example.chat.domain.chatroom.service;

import com.example.chat.domain.chatroom.domain.ChatRoom;
import com.example.chat.domain.chatroom.domain.ParticipationRoom;
import com.example.chat.domain.chatroom.service.port.ParticipationChatRoomRepository;
import com.example.chat.domain.chatroom.service.port.ParticipationRoomService;
import com.example.chat.domain.member.domain.Member;
import com.example.chat.domain.common.domain.Exception.CustomNoSuchElementException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParticipationRoomServiceImpl implements ParticipationRoomService {
    private final ParticipationChatRoomRepository participationRoomRepository;

    @Override
    public ParticipationRoom getByMemberAndRoom(Member member, ChatRoom room) {
        return participationRoomRepository.getByMemberAndRoom(member, room).orElse(null);
    }

    @Override
    public boolean existParticipationRoom(Member member, ChatRoom room) {
        return participationRoomRepository.getByMemberAndRoom(member, room).isEmpty();
    }

    @Override
    @Transactional
    public void join(Member member, ChatRoom room) {
        ParticipationRoom participationRoom = participationRoomRepository.getByMemberAndRoom(member, room)
                .orElseThrow(() -> new CustomNoSuchElementException("Participation info does not exist"));
        participationRoom = participationRoom.join(member, room);
        saveParticipationRoom(participationRoom);
    }

    @Transactional
    public void create(Member member, ChatRoom room) {
        ParticipationRoom participationRoom = ParticipationRoom.create(member, room);
        saveParticipationRoom(participationRoom);
    }

    @Override
    @Transactional
    public void submitSecretKey(Member member, ChatRoom room, String code) {
        ParticipationRoom participationRoom = ParticipationRoom.create(member, room);
        participationRoom = participationRoom.certificate(code);
        saveParticipationRoom(participationRoom);
    }

    private void saveParticipationRoom(ParticipationRoom participationRoom) {
        participationRoomRepository.save(participationRoom);
    }

    @Override
    @Transactional
    public boolean isCertifiedMember(Member member, ChatRoom room) {
        Optional<ParticipationRoom> participationRoom = participationRoomRepository
                .getByMemberAndRoom(member, room);

        return participationRoom.map(ParticipationRoom::isCertified).orElse(false);
    }

    @Transactional
    public boolean alreadyJoined(Member member, ChatRoom room) {
        Optional<ParticipationRoom> participationRoom = participationRoomRepository
                .getByMemberAndRoom(member, room);

        return participationRoom.map(ParticipationRoom::getJoinState).orElse(false);
    }

    @Override
    @Transactional
    public void deleteAllByRoom(ChatRoom room) {
        participationRoomRepository.deleteAllByRoom(room);
    }

    @Override
    @Transactional
    public void deleteByMemberAndRoom(Member member, ChatRoom room) {
        participationRoomRepository.deleteByMemberAndRoom(member, room);
    }
}
