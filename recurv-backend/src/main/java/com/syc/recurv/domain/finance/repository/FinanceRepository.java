package com.syc.recurv.domain.finance.repository;

import com.syc.recurv.domain.finance.entity.FinanceTransaction;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface FinanceRepository extends JpaRepository<FinanceTransaction, Long> {


    @Modifying
    @Query("INSERT INTO FinanceTransaction (partnerNo, amount, type, category, currency, createdAt) " +
            "VALUES (:partnerNo, :amount, 'INCOME', 'PAYMENT', 'KRW', CURRENT_TIMESTAMP)")
    void insertRevenue(@Param("partnerNo") Long partnerNo, @Param("amount") BigDecimal amount);
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO finance_transaction " +
            "(partner_no, payment_id, type, category, amount, currency, created_at) " +
            "VALUES (:partnerNo, NULL, 'EXPENSE', 'REFUND', :amount, 'KRW', NOW())",
            nativeQuery = true)
    void insertRefund(Long partnerNo, BigDecimal amount);
}
