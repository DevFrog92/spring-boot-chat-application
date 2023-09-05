package com.example.chat.domain.chatroom.controller;

import com.example.chat.domain.chatroom.controller.dto.request.RequestBanMemberDto;
import com.example.chat.domain.chatroom.controller.dto.request.RequestDto;
import com.example.chat.domain.chatroom.controller.dto.request.RequestSubmitCodeDto;
import com.example.chat.domain.chatroom.controller.dto.response.ChatRoomInfoResponseDto;
import com.example.chat.domain.chatroom.controller.dto.response.PermissionResponseDto;
import com.example.chat.domain.chatroom.controller.facade.ChatRoomFacade;
import com.example.chat.domain.chatroom.controller.facade.PermissionType;
import com.example.chat.domain.chatroom.dto.ChatRoomCreateDto;
import com.example.chat.domain.common.controller.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatRoomApiController {
    private final ChatRoomFacade chatRoomFacade;

    @GetMapping("/rooms")
    public ResponseEntity<?> rooms(Authentication auth) {
        String name = auth.getName();
        List<ChatRoomInfoResponseDto> allRooms =
                chatRoomFacade.findAllRooms(name);

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
        ChatRoomInfoResponseDto chatroomInfo =
                chatRoomFacade.getById(roomId);

        return new ResponseEntity<>(
                new ResponseDto<>(
                        "채팅방 정보를 조회했습니다.",
                        chatroomInfo
                ),
                HttpStatus.OK
        );
    }

    @PostMapping("/room")
    public ResponseEntity<?> createRoom(
            @RequestBody ChatRoomCreateDto chatRoomCreateDto) {

        ChatRoomInfoResponseDto chatroomInfo =
                chatRoomFacade.create(chatRoomCreateDto);

        return new ResponseEntity<>(
                new ResponseDto<>(
                        "채팅방 개설을 성공했습니다.",
                        chatroomInfo
                ),
                HttpStatus.OK
        );
    }

    @PostMapping("/key")
    public ResponseEntity<?> submitSecretKey(
            @RequestBody RequestSubmitCodeDto requestSubmitCode) {

        Long memberId = requestSubmitCode.getMemberId();
        Long roomId = requestSubmitCode.getRoomId();
        String code = requestSubmitCode.getSecretCode();

        chatRoomFacade.submitCode(memberId, roomId, code);

        return new ResponseEntity<>(
                new ResponseDto<>(
                        "인증에 성공했습니다.",
                        null
                ),
                HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/room")
    public ResponseEntity<?> deleteRoom(@RequestBody RequestDto requestDto) {
        Long memberId = requestDto.getRequestMemberId();
        Long roomId = requestDto.getRoomId();

        chatRoomFacade.delete(memberId, roomId);

        return new ResponseEntity<>(
                new ResponseDto<>(
                        "성공적으로 채팅방을 삭제했습니다",
                        null
                ),
                HttpStatus.OK);
    }

    @PostMapping("/permission")
    public ResponseEntity<?> checkPermission(@RequestBody RequestDto requestDto) {
        PermissionType type = chatRoomFacade.checkPermission(
                requestDto.getRoomId(), requestDto.getRequestMemberId());

        // todo What about occur exception ?
        String message = type.equals(PermissionType.ALLOW)
                ? "인증에 성공했습니다."
                : "인증에 실패했습니다.";

        return new ResponseEntity<>(
                new ResponseDto<>(
                        message,
                        PermissionResponseDto.from(type)
                ),
                HttpStatus.OK);
    }

    @PostMapping("/room/{roomId}/join")
    public ResponseEntity<?> join(@RequestBody RequestDto requestDto) {
        chatRoomFacade.enter(requestDto.getRequestMemberId(), requestDto.getRoomId());

        return new ResponseEntity<>(
                new ResponseDto<>(
                        "성공적으로 입장했습니다.",
                        null
                ),
                HttpStatus.OK
        );
    }

    @PostMapping("/room/{roomId}/leave")
    public ResponseEntity<?> leave(@RequestBody RequestDto requestDto) {
        chatRoomFacade.leave(requestDto.getRequestMemberId(), requestDto.getRoomId());

        return new ResponseEntity<>(
                new ResponseDto<>(
                        "성공적으로 퇴장했습니다.",
                        null
                ),
                HttpStatus.OK
        );
    }

    @PostMapping("/room/{roomId}/ban/{memberName}")
    public ResponseEntity<?> ban(@RequestBody RequestBanMemberDto banMemberDto) {
        chatRoomFacade.ban(banMemberDto.getBanMemberName(), banMemberDto.getRoomId());

        return new ResponseEntity<>(
                new ResponseDto<>(
                        "성공적으로 강퇴했습니다.",
                        null
                ),
                HttpStatus.OK
        );
    }
}
