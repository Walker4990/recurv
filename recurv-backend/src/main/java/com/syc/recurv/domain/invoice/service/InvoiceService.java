package com.syc.recurv.domain.invoice.service;

import com.syc.recurv.domain.invoice.entity.Invoice;
import com.syc.recurv.domain.invoice.repository.InvoiceRepository;
import com.syc.recurv.domain.subscription.entity.Subscription;
import com.syc.recurv.domain.subscription.repository.SubscriptionRepository;
import com.syc.recurv.domain.subscription.value.BillingInfo;
import com.syc.recurv.domain.subscription.value.Period;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
@CacheConfig(cacheNames = "invoice:list")
@Service
@RequiredArgsConstructor
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final SubscriptionRepository subscriptionRepository;
    // InvoiceService.java
    @Transactional
    @CacheEvict(allEntries = true)
    public Invoice createInvoice(Long partnerNo, Long planId, BigDecimal amount) {
        // 1) partnerNo 로 기존 구독 있는지 확인
        Subscription sub = subscriptionRepository.findByPartnerNo(partnerNo)
                .orElseGet(() -> {
                    // 없으면 새로 생성
                    Subscription newSub = Subscription.builder()
                            .partnerNo(partnerNo)
                            .status("PENDING")
                            .period(new Period(LocalDate.now(), LocalDate.now().plusMonths(1))) // 기간 기본값
                            .billingInfo(new BillingInfo("MONTHLY", (byte)1, LocalDate.now().plusMonths(1)))
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .build();
                    return subscriptionRepository.save(newSub);
                });

        // 2) Invoice 생성
        String orderId = "order_" + UUID.randomUUID();

        Invoice invoice = Invoice.builder()
                .invoiceNo(orderId)
                .subscriptionId(sub.getSubscriptionId())   // ✅ 여기서 subscriptionId 채워줌
                .partnerNo(partnerNo)
                .planId(planId)
                .issueDate(LocalDateTime.now())
                .dueDate(LocalDate.now().plusDays(7))
                .status("PENDING")
                .subtotalAmount(amount)
                .taxAmount(BigDecimal.ZERO)
                .totalAmount(amount)
                .currency("KRW")
                .build();

        return invoiceRepository.save(invoice);
    }


    @Cacheable(key = "'invoice:' + #id")
    public Invoice get(Long id) {
        return invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("인보이스 없음"));
    }
    @Cacheable(key = "'invoice:' + #id")
    public List<Invoice> getAll() {
        return invoiceRepository.findAll();
    }
    @CacheEvict(allEntries = true)
    public Invoice update(Invoice invoice) {
        return invoiceRepository.save(invoice);
    }
    @CacheEvict(allEntries = true)
    public void delete(Long id) {
        invoiceRepository.deleteById(id);
    }
}

