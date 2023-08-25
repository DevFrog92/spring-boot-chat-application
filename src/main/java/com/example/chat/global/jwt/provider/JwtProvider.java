package com.example.chat.global.jwt.provider;

public interface JwtProvider {
    String generateToken(String key);
    String getKeyFromToken(String token);
    void validateToken(String token);
}
