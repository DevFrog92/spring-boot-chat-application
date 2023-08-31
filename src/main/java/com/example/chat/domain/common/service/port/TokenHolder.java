package com.example.chat.domain.common.service.port;

public interface TokenHolder {
    String generateToken(String key);
    String getKeyFromToken(String token);
    void validateToken(String token);
}
