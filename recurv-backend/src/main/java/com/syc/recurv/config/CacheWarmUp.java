package com.syc.recurv.config;

import com.syc.recurv.domain.users.service.UsersAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CacheWarmUp {

    private final UsersAdminService usersAdminService;

    // 3분마다 파트너 목록 캐시를 자동 재빌드
    @Scheduled(fixedRate = 180000)
    public void warmPartners() {
        usersAdminService.getAllPartners(); // @Cacheable 메서드 호출
    }
}
