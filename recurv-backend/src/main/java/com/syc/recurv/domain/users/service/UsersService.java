package com.syc.recurv.domain.users.service;

import com.syc.recurv.domain.users.entity.Users;
import com.syc.recurv.domain.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsersService {
    private final UsersRepository usersRepository;
    // 생성
    public Users create(Users users){
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
