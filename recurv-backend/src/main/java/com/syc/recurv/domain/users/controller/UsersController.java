package com.syc.recurv.domain.users.controller;

import com.syc.recurv.domain.users.entity.Users;
import com.syc.recurv.domain.users.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class UsersController {
    private final UsersService usersService;

    // 회원가입
    @PostMapping
    public ResponseEntity<Users> register(@RequestBody Users users){
        return ResponseEntity.ok(usersService.create(users));
    }
    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Users loginRequest) {
        System.out.println(loginRequest.getPassword() +  loginRequest.getUserIdStr());
        Users user = usersService.findByUserIdStr(loginRequest.getUserIdStr())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 이메일입니다."));

        if (!user.getPassword().equals(loginRequest.getPassword())) {
            return ResponseEntity.status(401).body("비밀번호가 틀렸습니다.");
        }

        return ResponseEntity.ok(user);
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

