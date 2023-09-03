package com.example.chat.domain.chatroom.service;

import com.example.chat.domain.chatroom.domain.ChatRoom;
import com.example.chat.domain.chatroom.dto.ChatRoomCreateDto;
import com.example.chat.domain.chatroom.service.port.ChatRoomRepository;
import com.example.chat.domain.chatroom.service.port.ChatRoomService;
import com.example.chat.domain.common.Exception.CustomRuntimeException;
import com.example.chat.domain.member.domain.Member;
import com.example.chat.domain.common.Exception.CustomNoSuchElementException;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@Builder
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

    @Override
    @Transactional
    public ChatRoom create(Member member, ChatRoomCreateDto chatRoomCreateDto) {
        ChatRoom chatRoom = ChatRoom.create(member, chatRoomCreateDto);
        return chatRoomRepository.save(chatRoom);
    }

    @Override
    @Transactional
    public void updateParticipationNum(ChatRoom chatRoom) {
        chatRoomRepository.save(chatRoom);
    }

    @Override
    public List<ChatRoom> findAllRooms(Member member) {
        return chatRoomRepository.findAllRooms(member);
    }

    @Override
    @Transactional
    public void deleteRoom(Member member, ChatRoom chatRoom) {
        if (!Objects.equals(chatRoom.getOwnerId(), member.getId())) {
            throw new CustomRuntimeException("채팅방 관리자만 채팅방을 삭제할 수 있습니다.");
        }

        chatRoomRepository.deletById(chatRoom.getId());
    }

    @Override
    public ChatRoom getById(Long id) {
        return chatRoomRepository.getById(id)
                .orElseThrow(() -> new CustomNoSuchElementException("요청한 ID의 채팅방을 찾을 수 없습니다"));
    }
}
