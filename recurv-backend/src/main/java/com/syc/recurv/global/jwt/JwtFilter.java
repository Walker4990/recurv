package com.syc.recurv.global.jwt;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();


        // ✅ 테스트용 엔드포인트는 JWT 검사 생략
        if (path.startsWith("/test/")
                || path.startsWith("/ws")
                || path.startsWith("/api/payment/webhook")
                || path.startsWith("/api/payment/request")) {
            filterChain.doFilter(request, response);
            return;
        }
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            if (jwtUtil.validateToken(token)) {
                Claims claims = jwtUtil.getClaims(token);
                Long userId = claims.get("userId", Long.class);
                String nickname = claims.get("nickname", String.class);
                String role = claims.get("role", String.class);

                // ROLE_ 중복 방지
                if (!role.startsWith("ROLE_")) {
                    role = "ROLE_" + role;
                }

                System.out.println("Authorization header: " + authHeader);
                System.out.println("JWT Role(raw) = " + claims.get("role"));
                System.out.println("JWT Role(final) = " + role);

                // 권한 리스트 생성
                List<SimpleGrantedAuthority> authorities =
                        Collections.singletonList(new SimpleGrantedAuthority(role));

                // 인증 객체 생성
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userId, null, authorities);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // SecurityContext에 등록
                SecurityContextHolder.getContext().setAuthentication(authentication);

                // 등록 확인 로그
                System.out.println("Authentication after set: "
                        + SecurityContextHolder.getContext().getAuthentication());
            }
        }

        filterChain.doFilter(request, response);
    }
}
