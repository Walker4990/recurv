package com.syc.recurv.domain.subscription.repository;

import com.syc.recurv.domain.subscription.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    // ✅ 파트너별 활성화/비활성화
    @Modifying
    @Query("UPDATE Subscription s SET s.status = 'ACTIVE' WHERE s.partnerNo = :partnerNo")
    void activateByPartnerNo(@Param("partnerNo") Long partnerNo);

    @Modifying
    @Query("UPDATE Subscription s SET s.status = 'INACTIVE' WHERE s.partnerNo = :partnerNo")
    void deactivateByPartnerNo(@Param("partnerNo") Long partnerNo);

    // ✅ 단건 조회/존재 확인
    Optional<Subscription> findByPartnerNo(Long partnerNo);

    boolean existsByPartnerNo(Long partnerNo);

    // ✅ 구독 만료일 기준 조회 (Period.endDate 기준)
    @Query("SELECT s FROM Subscription s WHERE s.period.endDate <= :targetDate AND s.status = 'ACTIVE'")
    List<Subscription> findExpiringSubscriptions(@Param("targetDate") LocalDate targetDate);

    // ✅ 단일 날짜 일치 (JPA 자동 쿼리 버전)
    List<Subscription> findByPeriod_EndDate(LocalDate targetDate);
}
