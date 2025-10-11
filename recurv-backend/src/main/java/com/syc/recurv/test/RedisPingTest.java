package com.syc.recurv.test; // ⚠️ 패키지명 네 프로젝트 구조에 맞춰 조정

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisPingTest implements CommandLineRunner {

    private final StringRedisTemplate redisTemplate;

    @Override
    public void run(String... args) {
        redisTemplate.opsForValue().set("ping:test", "PONG");
        String value = redisTemplate.opsForValue().get("ping:test");
        System.out.println("✅ Redis Test Result: " + value);
    }
}
