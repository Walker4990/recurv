package com.syc.recurv.domain.support.repository;

import com.syc.recurv.domain.support.entity.SupportMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SupportMessageRepository extends JpaRepository<SupportMessage, Long> {
    // 최근 20개 메시지 (관리자 대시보드용)
    List<SupportMessage> findTop20ByOrderByCreatedAtDesc();

    // 최근 대화가 있는 파트너 목록
    @Query("SELECT DISTINCT s.partnerNo FROM SupportMessage s ORDER BY MAX(s.createdAt) DESC")
    List<Long> findRecentPartnerNos();

    // 특정 파트너와의 대화 이력 불러오기 (채팅방용)
    List<SupportMessage> findByPartnerNoOrderByCreatedAtAsc(Long partnerNo);
}
