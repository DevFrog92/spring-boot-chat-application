package com.example.chat.service;

import com.example.chat.domain.Member;
import com.example.chat.domain.ParticipationRoom;
import com.example.chat.domain.Room;
import com.example.chat.repository.ParticipationRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParticipationRoomService {
    private final ParticipationRoomRepository participationRoomRepository;

    @Transactional
    public void join(Member member, Room room) {
        ParticipationRoom participationRoom =
                participationRoomRepository.findByMemberAndRoom(member, room).orElseThrow();
        participationRoom.setFirstJoin(true);
    }

    @Transactional
    public void submitSecretKey(Member member, Room room) {
        ParticipationRoom chatRoom = ParticipationRoom.builder()
                .member(member)
                .room(room)
                .submitKey(true)
                .build();

        participationRoomRepository.save(chatRoom);
    }

    @Transactional
    public boolean isPermitMember(Member member, Room room) {
        Optional<ParticipationRoom> participationRoom =
                participationRoomRepository.findByMemberAndRoom(member, room);

        if(participationRoom.isPresent()) {
            return participationRoom.get().getSubmitKey();
        }

        return false;
    }

    @Transactional
    public boolean isFirstJoinChatRoom(Member member, Room room) {
        Optional<ParticipationRoom> participationRoom =
                participationRoomRepository.findByMemberAndRoom(member, room);
        if(participationRoom.isPresent()) {
            return participationRoom.get().getJoined();
        }

        return false;
    }
}
