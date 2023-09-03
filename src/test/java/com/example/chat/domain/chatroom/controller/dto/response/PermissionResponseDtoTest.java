package com.example.chat.domain.chatroom.controller.dto.response;

import org.junit.jupiter.api.Test;

import static com.example.chat.domain.chatroom.controller.facade.PermissionType.NEED_PASSWORD;
import static org.assertj.core.api.Assertions.assertThat;

class PermissionResponseDtoTest {
    @Test
    void from_메서드로_PermissionType_을_파라미터로_받아_PermissionResponseDto_객체를_생성할_수_있다() {
        //given
        //when
        PermissionResponseDto result = PermissionResponseDto.from(NEED_PASSWORD);

        //then
        assertThat(result.getType()).isEqualTo(NEED_PASSWORD);
    }
}
