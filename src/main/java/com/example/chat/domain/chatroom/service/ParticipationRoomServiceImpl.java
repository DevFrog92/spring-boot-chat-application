package com.example.chat.domain.chatroom.service;

import com.example.chat.domain.chatroom.domain.ChatRoom;
import com.example.chat.domain.chatroom.domain.ParticipationRoom;
import com.example.chat.domain.chatroom.service.port.ParticipationChatRoomRepository;
import com.example.chat.domain.chatroom.service.port.ParticipationRoomService;
import com.example.chat.domain.common.Exception.CustomIncorrectPassword;
import com.example.chat.domain.member.domain.Member;
import com.example.chat.domain.common.Exception.CustomNoSuchElementException;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@Builder
@RequiredArgsConstructor
public class ParticipationRoomServiceImpl implements ParticipationRoomService {
    private final ParticipationChatRoomRepository participationRoomRepository;

    @Override
    public ParticipationRoom getByMemberAndRoom(Member member, ChatRoom room) {
        return participationRoomRepository
                .getByMemberAndRoom(member, room).orElse(null);
    }

    @Override
    @Transactional
    public void join(Member member, ChatRoom room) {
        ParticipationRoom participationRoom = participationRoomRepository
                .getByMemberAndRoom(member, room)
                .orElseThrow(CustomNoSuchElementException::new);
        participationRoom = participationRoom.join(member, room);
        saveParticipationRoom(participationRoom);
    }

    @Transactional
    public void create(Member member, ChatRoom room) {
        ParticipationRoom participationRoom =
                ParticipationRoom.create(member, room);
        saveParticipationRoom(participationRoom);
    }

    @Override
    @Transactional
    public void submitSecretKey(Member member, ChatRoom chatRoom, String code) {
        if(!chatRoom.checkCode(code)) {
            throw new CustomIncorrectPassword();
        }

        ParticipationRoom participationRoom = ParticipationRoom.create(member, chatRoom);
        participationRoom = participationRoom.certification();
        saveParticipationRoom(participationRoom);
    }

    private void saveParticipationRoom(ParticipationRoom participationRoom) {
        participationRoomRepository.save(participationRoom);
    }

    @Override
    @Transactional
    public boolean isCertifiedMember(Member member, ChatRoom room) {
        Optional<ParticipationRoom> participationRoom =
                participationRoomRepository.getByMemberAndRoom(member, room);

        return participationRoom
                .map(ParticipationRoom::getSubmitKey)
                .orElse(false);
    }

    @Transactional
    public boolean alreadyJoined(Member member, ChatRoom room) {
        Optional<ParticipationRoom> participationRoom =
                participationRoomRepository.getByMemberAndRoom(member, room);

        return participationRoom
                .map(ParticipationRoom::getJoined)
                .orElse(false);
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
