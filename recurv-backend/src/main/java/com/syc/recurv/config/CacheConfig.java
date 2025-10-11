package com.syc.recurv.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableCaching(proxyTargetClass = true)
@EnableAspectJAutoProxy(exposeProxy = true)
public class CacheConfig {

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory rcf) {

        // ✅ LocalDateTime 직렬화 지원
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // ✅ 공통 직렬화 설정 + 기본 TTL 5분
        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(
                        new GenericJackson2JsonRedisSerializer(objectMapper)))
                .disableCachingNullValues()
                .entryTtl(Duration.ofMinutes(5)); // 기본 TTL 5분

        // ✅ 캐시별 세부 TTL 설정
        Map<String, RedisCacheConfiguration> cacheConfigs = new HashMap<>();

        // 🧩 파트너 목록 (변동 적음 → 3분)
        cacheConfigs.put("partners:all", defaultConfig.entryTtl(Duration.ofMinutes(3)));

        // 🧩 구독 목록 (자주 변경 → 2분)
        cacheConfigs.put("subscription:list", defaultConfig.entryTtl(Duration.ofMinutes(2)));

        // 🧩 인보이스 목록 (통계성 → 5분)
        cacheConfigs.put("invoice:list", defaultConfig.entryTtl(Duration.ofMinutes(5)));

        // 🧩 결제 요약 (통계 갱신 주기 고려 → 10분)
        cacheConfigs.put("payment:summary", defaultConfig.entryTtl(Duration.ofMinutes(10)));

        // ✅ RedisCacheManager 빌드
        return RedisCacheManager.builder(rcf)
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(cacheConfigs)
                .transactionAware()
                .build();
    }
}
