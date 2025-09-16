package com.syc.recurv.domain.payment.service;

import com.syc.recurv.domain.payment.entity.PaymentMethod;
import com.syc.recurv.domain.payment.repository.PaymentMethodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentMethodService {
    private final PaymentMethodRepository paymentMethodRepository;

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

