package com.syc.recurv.global.jwt;

import com.syc.recurv.domain.users.entity.Users;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {

    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final long EXPIRATION_TIME = 1000 * 60 * 60; // 1시간

    // 토큰 생성
    // JwtUtil.java
    public String generateToken(Long userId, String userIdStr, String nickname, String role) {
        return Jwts.builder()
                .setSubject(userIdStr)
                .claim("userId", userId)
                .claim("nickname", nickname)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }


    public String getNickname(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody()
                .get("nickname", String.class);
    }


    // 토큰 유효성 검사
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public Long getUserId(String token) {
        return Long.parseLong(
                Jwts.parserBuilder().setSigningKey(key).build()
                        .parseClaimsJws(token).getBody().get("userId").toString()
        );
    }
    // 토큰에서 클레임 꺼내기
    public Claims getClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody();
    }
}
