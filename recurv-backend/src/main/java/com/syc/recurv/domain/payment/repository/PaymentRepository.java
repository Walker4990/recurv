package com.syc.recurv.domain.payment.repository;

import com.syc.recurv.domain.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,Long> {
    // 1) PG에서 내려준 orderId로 찾기
    Optional<Payment> findByOrderId(String orderId);
    // 2) 내부 invoiceId 기준으로 찾기
    Optional<Payment> findByInvoiceId(Long invoiceId);
    // 3) 특정 partnerNo의 가장 최근 결제 조회
    Optional<Payment> findTopByPartnerNoOrderByApprovedAtDesc(Long partnerNo);

    @Query("SELECT p FROM Payment p WHERE p.partnerNo = :partnerNo")
    List<Payment> findByPartnerNo(@Param("partnerNo") Long partnerNo);
}
