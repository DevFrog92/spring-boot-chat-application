package com.example.chat.domain.chatroom.controller.dto.response;

import com.example.chat.domain.chatroom.controller.facade.PermissionType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PermissionResponseDto {
    private PermissionType type;

    @Builder
    public PermissionResponseDto(PermissionType type) {
        this.type = type;
    }

    public static PermissionResponseDto from(PermissionType type) {
        return PermissionResponseDto.builder()
                .type(type)
                .build();
    }
}
