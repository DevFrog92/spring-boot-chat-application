package com.example.chat.domain.chatroom.controller;

import com.example.chat.domain.chatroom.dto.chatroom.CreateRoomDto;
import com.example.chat.domain.chatroom.dto.message.ChatNoticeDto;
import com.example.chat.global.web.dto.ResponseDto;
import com.example.chat.domain.chatroom.dto.chatroom.RoomInfoDto;
import com.example.chat.domain.chatroom.dto.chatroom.SubmitSecretKeyDto;
import com.example.chat.domain.chatroom.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.chat.domain.chatroom.dto.chatroom.ChatRoomType.PUBLIC;

@Slf4j
@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatRoomApiController {
    private final ChatRoomService chatRoomService;

    @GetMapping("/rooms")
    public ResponseEntity<?> rooms(Authentication auth) {
        List<RoomInfoDto> allRooms = chatRoomService.findAllRooms(auth.getName());

        return new ResponseEntity<>(
                new ResponseDto<>(
                        "모든 채팅방 조회를 성공했습니다.",
                        allRooms
                ),
                HttpStatus.OK
        );
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<?> roomInfo(@PathVariable Long roomId) {
        RoomInfoDto roomInfo = chatRoomService.getRoomInfo(roomId);
        return new ResponseEntity<>(
                new ResponseDto<>(
                        "채팅방 정보를 조회했습니다.",
                        roomInfo
                ),
                HttpStatus.OK
        );
    }

    @PostMapping("/room")
    public ResponseEntity<?> createRoom(@RequestBody CreateRoomDto dto) {
        RoomInfoDto room;
        if (dto.getType().equals(PUBLIC)) {
            room = chatRoomService.createPublicRoom(dto);
        }else {
            room = chatRoomService.createPrivateRoom(dto);
        }

        return new ResponseEntity<>(
                new ResponseDto<>(
                        "채팅방 개설을 성공했습니다.",
                        room
                ),
                HttpStatus.OK
        );
    }

    @PostMapping("/key")
    public ResponseEntity<?> submitSecretKey(@RequestBody SubmitSecretKeyDto dto) {
        boolean result = chatRoomService.checkSubmitSecretKey(dto);
        HttpStatus status = result ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        String message = result ? "인증에 성공했습니다." : "비밀번호를 확인해주세요.";
        return new ResponseEntity<>(
                new ResponseDto<>(
                        message,
                        null
                ),
                status);
    }

    @PostMapping("/room/credential")
    public ResponseEntity<?> getMemberCredentialOfChatRoom(@RequestBody SubmitSecretKeyDto dto) {
        boolean isPermitted = chatRoomService.isPermitMemberEnterChatRoom(
                dto.getRoomId(),
                dto.getMemberId());

        return new ResponseEntity<>(
                new ResponseDto<>(
                        "신원 조회를 완료했습니다.",
                        isPermitted
                ),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/room")
    public void deleteRoom(@RequestBody ChatNoticeDto dto) {
        chatRoomService.deleteRoom(dto);
    }
}
