package com.syc.recurv.domain.subscription.service;

import com.syc.recurv.domain.finance.entity.FinanceTransaction;
import com.syc.recurv.domain.finance.repository.FinanceRepository;
import com.syc.recurv.domain.payment.entity.Payment;
import com.syc.recurv.domain.payment.repository.PaymentRepository;
import com.syc.recurv.domain.payment.service.PaymentService;
import com.syc.recurv.domain.subscription.controller.SubscriptionSocketController;
import com.syc.recurv.domain.subscription.entity.Subscription;
import com.syc.recurv.domain.subscription.repository.SubscriptionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
@CacheConfig(cacheNames = "subscription:list")
@Service
@RequiredArgsConstructor
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentService paymentService;
    private final FinanceRepository financeRepository;
    private final SubscriptionSocketController socketController;

    // create 구독 생성 시 캐시 무효화
    @CacheEvict(allEntries = true)
    public Subscription create (Subscription sc){
        return subscriptionRepository.save(sc);
    }

    // Read
    public Subscription get(Long id) {
        return subscriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("구독 없음"));
    }
    // Read All
    @Cacheable(key = "'allSubscriptions'")
    public List<Subscription> getAll(){
        return  subscriptionRepository.findAll();
    }
    // Update
    @CacheEvict(allEntries = true)
    public Subscription update(Subscription sc){
        return subscriptionRepository.save(sc);
    }
    // Delete
    @Transactional
    @CacheEvict(allEntries = true)
    public void cancelSubscription (Long subscriptionId){
        // 구독 찾기 -> 결제 조회 -> 결제 취소
        Subscription sub = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new RuntimeException("구독을 찾을 수 없습니다."));

        Payment payment = paymentRepository.findTopByPartnerNoOrderByApprovedAtDesc(sub.getPartnerNo())
                .orElseThrow(() -> new RuntimeException("결제 정보를 찾을 수 없습니다."));
        // 3) Toss 환불 API 호출
        boolean refunded = paymentService.refundWithToss(payment.getPaymentKey(), payment.getAmount());
        if (!refunded) {
            throw new RuntimeException("PG 환불 실패");
        }

        // 4) 결제 상태 변경
        payment.setStatus("REFUNDED");
        paymentRepository.save(payment);

        // 5) 재무 기록 (지출 처리)
        financeRepository.save(FinanceTransaction.builder()
                .payment(payment)
                .partnerNo(payment.getPartnerNo())
                .type("EXPENSE")
                .category("REFUND")
                .amount(payment.getAmount())
                .currency(payment.getCurrency())
                .createdAt(LocalDateTime.now())
                .build()
        );

        // 6) 구독 상태 변경 (삭제 or CANCELED)
        sub.setStatus("CANCELED");   // ❗ 삭제보다는 상태값 변경 권장
        subscriptionRepository.save(sub);
    }


    // WebSocket 활용
    @Transactional
    @CacheEvict(allEntries = true)
    public void activateSubscription(Long id) {
        Subscription sub = subscriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subscription not found"));
        sub.setStatus("ACTIVE");
        subscriptionRepository.save(sub);
        // WebSocket 방송
        socketController.broadcastSubscriptionUpdate(
                sub.getPartnerNo(),                 // ERP 파트너 번호
                sub.getBillingInfo().getBillingCycle(), // 과금 주기
                sub.getStatus()                     // 상태 (ACTIVE 등)
        );
    }

}
