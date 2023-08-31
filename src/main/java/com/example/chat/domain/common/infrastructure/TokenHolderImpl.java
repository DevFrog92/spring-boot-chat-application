package com.example.chat.domain.common.infrastructure;

import com.example.chat.domain.common.service.port.JwtHolder;
import com.example.chat.domain.common.service.port.TokenHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenHolderImpl implements TokenHolder {

    private final JwtHolder jwtHolder;

    @Override
    public String generateToken(String key) {
        return jwtHolder.generateToken(key);
    }

    @Override
    public String getKeyFromToken(String token) {
        return jwtHolder.getKeyFromToken(token);
    }

    @Override
    public void validateToken(String token) {
        jwtHolder.validateToken(token);
    }
}
