package com.syc.recurv.domain.users.repository;

import com.syc.recurv.domain.users.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByUserIdStr(String userIdStr);

    boolean existsByEmail(String email);

    boolean existsByUserIdStr(String userIdStr);

    boolean existsByNickname(String nickname);

    boolean existsByPhone(String phone);
}
