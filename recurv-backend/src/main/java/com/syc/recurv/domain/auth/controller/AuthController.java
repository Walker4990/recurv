package com.syc.recurv.domain.auth.controller;

import com.syc.recurv.domain.users.entity.Users;
import com.syc.recurv.domain.users.repository.UsersRepository;
import com.syc.recurv.global.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UsersRepository usersRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        String userIdStr = loginRequest.get("userIdStr");
        String password = loginRequest.get("password");

        Users user = usersRepository.findByUserIdStr(userIdStr)
                .orElseThrow(() -> new IllegalArgumentException("아이디가 존재하지 않습니다."));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        String token = jwtUtil.generateToken(
                user.getUserId(),
                user.getUserIdStr(),
                user.getNickname(),
                user.getRole()
        );
        System.out.println("===== login controller 들어옴 =====");
        System.out.println("user: " + user);
        System.out.println("token: " + token);
        System.out.println("res 직전까지 실행됨");
        Map<String, Object> res = new HashMap<>();
        res.put("token", token);
        res.put("userId", user.getUserId());
        res.put("userIdStr",  userIdStr);
        res.put("role",  user.getRole());
        res.put("nickname", user.getNickname());
        System.out.println(res);
        return ResponseEntity.ok(res);

    }
}
