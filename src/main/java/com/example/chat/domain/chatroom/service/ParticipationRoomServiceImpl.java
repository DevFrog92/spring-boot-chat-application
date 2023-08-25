package com.example.chat.domain.chatroom.service;

import com.example.chat.domain.chatroom.domain.ParticipationRoom;
import com.example.chat.domain.chatroom.dto.chatroom.RoomDto;
import com.example.chat.global.web.Exception.CustomNoSuchElementException;
import com.example.chat.domain.member.dto.MemberDto;
import com.example.chat.domain.chatroom.domain.ParticipationRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParticipationRoomServiceImpl implements ParticipationRoomService {
    private final ParticipationRoomRepository participationRoomRepository;

    @Override
    @Transactional
    public void join(MemberDto member, RoomDto room) {
        ParticipationRoom participationRoom = participationRoomRepository
                .findByMemberAndRoom(
                        member.getEntity(),
                        room.getEntity())
                .orElseThrow(() ->
                        new CustomNoSuchElementException(
                                "Participation room does not exist"));

        participationRoom.setFirstJoin(true);
    }

    @Override
    @Transactional
    public void createParticipationRelation(MemberDto member, RoomDto room) {
        participationRoomRepository.save(
                ParticipationRoom.builder()
                        .member(member.getEntity())
                        .room(room.getEntity())
                        .joined(true)
                        .submitKey(true)
                        .build()
        );
    }

    @Override
    @Transactional
    public void submitSecretKey(MemberDto member, RoomDto room) {
        ParticipationRoom chatRoom = ParticipationRoom.builder()
                .member(member.getEntity())
                .room(room.getEntity())
                .joined(false)
                .submitKey(true)
                .build();

        participationRoomRepository.save(chatRoom);
    }

    @Override
    @Transactional
    public boolean isPermitMember(MemberDto member, RoomDto room) {
        Optional<ParticipationRoom> participationRoom =
                participationRoomRepository.findByMemberAndRoom(
                        member.getEntity(),
                        room.getEntity()
                );

        if (participationRoom.isPresent()) {
            return participationRoom.get().getSubmitKey();
        }

        return false;
    }

    @Override
    @Transactional
    public boolean alreadyJoinChatRoom(MemberDto member, RoomDto room) {
        Optional<ParticipationRoom> participationRoom =
                participationRoomRepository.findByMemberAndRoom(
                        member.getEntity(),
                        room.getEntity()
                );

        if (participationRoom.isPresent()) {
            return participationRoom.get().getJoined();
        }

        return false;
    }

    @Override
    @Transactional
    public void deleteParticipationRelationByRoom(RoomDto room) {
        participationRoomRepository.deleteAllByRoom(room.getEntity());
    }

    @Override
    @Transactional
    public void deleteParticipationRelationByRoomAndMember(RoomDto room,
                                                           MemberDto member) {
        participationRoomRepository.deleteByRoomAndMember(
                room.getEntity(),
                member.getEntity()
        );
    }
}
