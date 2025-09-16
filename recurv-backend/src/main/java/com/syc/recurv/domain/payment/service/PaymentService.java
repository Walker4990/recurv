package com.syc.recurv.domain.payment.service;

import com.syc.recurv.domain.payment.entity.Payment;
import com.syc.recurv.domain.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;

    public Payment create(Payment payment) {
        return paymentRepository.save(payment);
    }

    public Payment get(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("결제 없음"));
    }

    public List<Payment> getAll() {
        return paymentRepository.findAll();
    }

    public Payment update(Payment payment) {
        return paymentRepository.save(payment);
    }

    public void delete(Long id) {
        paymentRepository.deleteById(id);
    }
}

