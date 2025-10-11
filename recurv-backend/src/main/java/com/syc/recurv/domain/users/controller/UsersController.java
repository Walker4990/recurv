package com.syc.recurv.domain.users.controller;

import com.syc.recurv.domain.users.entity.Users;
import com.syc.recurv.domain.users.repository.UsersRepository;
import com.syc.recurv.domain.users.service.UsersService;
import com.syc.recurv.global.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class UsersController {
    private final UsersService usersService;
    private final JwtUtil jwtUtil;
    private final UsersRepository usersRepository;
    private PasswordEncoder passwordEncoder;

    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<Users> register(@RequestBody Users users){
        Users user = usersService.create(users);
        return ResponseEntity.ok(user);
    }
    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Users loginRequest) {
        Users user = usersService.findByUserIdStr(loginRequest.getUserIdStr())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 아이디입니다."));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).body("비밀번호가 틀렸습니다.");
        }
        // 3️⃣ JWT 토큰 발급
        String token = jwtUtil.generateToken(
                user.getUserId(),
                user.getUserIdStr(),
                user.getName(),
                user.getRole()
        );

        Map<String, Object> res = new HashMap<>();
        res.put("token", token);
        res.put("userId", user.getUserId());
        res.put("name", user.getName());
        res.put("role", user.getRole());


        return ResponseEntity.ok(user);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰 없음");
        }
        String token = authHeader.substring(7);
        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰");
        }
        // 토큰으로 가져온 userId
        Long userId = jwtUtil.getUserId(token);
        String nickname = jwtUtil.getNickname(token);

        // DB 조회
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자 없음"));

        // 응답
        Map<String,Object> res = new HashMap<>();
        res.put("userId", userId);
        res.put("userIdStr", user.getUserIdStr());
        res.put("nickname", nickname);
        res.put("role", user.getRole());
        res.put("email", user.getEmail());

        return ResponseEntity.ok(res);
    }

    // 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<Users> get(@PathVariable Long id) {
        return ResponseEntity.ok(usersService.get(id));
    }

    // 전체 조회
    @GetMapping
    public ResponseEntity<List<Users>> findAll() {
        return ResponseEntity.ok(usersService.fiadAll());
    }

    // 수정
    @PutMapping("/{id}")
    public ResponseEntity<Users> update(@PathVariable Long userId, @RequestBody Users users) {
        users.setUserId(userId); // PK 세팅
        return ResponseEntity.ok(usersService.update(users));
    }

    // 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        usersService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

