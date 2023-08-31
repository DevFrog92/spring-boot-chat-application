package com.example.chat.domain.common.infrastructure;

import com.example.chat.domain.common.service.port.JwtHolder;
import com.example.chat.domain.common.domain.Exception.CustomJwtException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@PropertySource("classpath:jwt.properties")
public class JwtHolderImpl implements JwtHolder {
    @Value("${jwt.secret.key}")
    private String secretKey;

    @Override
    public String generateToken(String key) {
        Date date = new Date();
        long validateTime = 1000L * 60 * 60;
        return Jwts.builder()
                .setId(key)
                .setIssuedAt(date)
                .setExpiration(new Date(date.getTime() + validateTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    @Override
    public String getKeyFromToken(String token) {
        return getClaims(token).getBody().getId();
    }

    @Override
    public void validateToken(String jwt) {
        this.getClaims(jwt);
    }

    private Jws<Claims> getClaims(String jwt) {
        try {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwt);
        } catch (RuntimeException ex) {
            throw new CustomJwtException(ex);
        }
    }
}
