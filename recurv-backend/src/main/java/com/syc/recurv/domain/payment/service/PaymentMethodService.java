package com.syc.recurv.domain.payment.service;

import com.syc.recurv.domain.payment.entity.PaymentMethod;
import com.syc.recurv.domain.payment.repository.PaymentMethodRepository;
import com.syc.recurv.domain.subscription.entity.Subscription;
import com.syc.recurv.domain.subscription.repository.SubscriptionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentMethodService {
    private final PaymentMethodRepository paymentMethodRepository;
    private final SubscriptionRepository subscriptionRepository;

    @Transactional
    public PaymentMethod changePaymentMethod(Long partnerNo, PaymentMethod newMethod) {
        // 1. 기존 결제 수단 해제
        paymentMethodRepository.findByPartnerNoAndIsDefaultTrue(partnerNo)
                .ifPresent(old -> {
                    old.setIsDefault(false);
                    paymentMethodRepository.save(old);
                });

        // 새 결제 수단 등록
        newMethod.setPartnerNo(partnerNo);
        newMethod.setIsDefault(true);
        PaymentMethod saved = paymentMethodRepository.save(newMethod);

        // 기존 결제 수단 해제
        paymentMethodRepository.findByPartnerNoAndIsDefaultTrue(partnerNo)
                .ifPresent(old -> {
                    old.setIsDefault(false);
                     paymentMethodRepository.save(old);
        });

        // 구독 갱신
        Subscription sub = subscriptionRepository.findByPartnerNo(partnerNo)
                .orElseThrow(() -> new RuntimeException("구독 없음"));
        sub.setPaymentMethodId(saved.getPaymentMethodId());
        subscriptionRepository.save(sub);
        return saved;
    }

    public PaymentMethod create(PaymentMethod method) {
        return paymentMethodRepository.save(method);
    }

    public PaymentMethod get(Long id) {
        return paymentMethodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("결제수단 없음"));
    }

    public List<PaymentMethod> getAll() {
        return paymentMethodRepository.findAll();
    }

    public PaymentMethod update(PaymentMethod method) {
        return paymentMethodRepository.save(method);
    }

    public void delete(Long id) {
        paymentMethodRepository.deleteById(id);
    }
}

