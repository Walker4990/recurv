package com.syc.recurv.domain.users.service;

import com.syc.recurv.domain.users.entity.Users;
import com.syc.recurv.domain.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsersService {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    // 생성
    public Users create(Users users){
        if (usersRepository.existsByEmail(users.getEmail())) {
            throw new RuntimeException("이미 사용중인 이메일입니다.");
        }
        if (usersRepository.existsByUserIdStr(users.getUserIdStr())){
            throw new RuntimeException("이미 사용중인 아이디입니다.");
        }
        // 3. 닉네임 중복 체크
        if (usersRepository.existsByNickname(users.getNickname())) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }

        // 4. 전화번호 중복 체크
        if (usersRepository.existsByPhone(users.getPhone())) {
            throw new IllegalArgumentException("이미 사용 중인 전화번호입니다.");
        }


        String encodedPassword = passwordEncoder.encode(users.getPassword());
        users.setPassword(encodedPassword);

        if (users.getRole() == null) {
            users.setRole("USER");
        }
        return usersRepository.save(users);
    }
    // 로그인
    public Optional<Users> findByUserIdStr(String userIdStr) {
        return usersRepository.findByUserIdStr(userIdStr);
    }
    // 단건 조회
    public Users get(Long id){
        return usersRepository.getOne(id);
    }

    // 전체 조회
    public List<Users> fiadAll(){
        return usersRepository.findAll();
    }
    // 정보 수정
    public Users update (Users users){
        return usersRepository.save(users);
    }
    public void delete(Long id){
        usersRepository.deleteById(id);
    }
}
