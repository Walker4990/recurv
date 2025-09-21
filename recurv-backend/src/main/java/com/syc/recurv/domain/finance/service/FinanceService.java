package com.syc.recurv.domain.finance.service;

import com.syc.recurv.domain.finance.entity.FinanceTransaction;
import com.syc.recurv.domain.finance.repository.FinanceRepository;
import com.syc.recurv.domain.payment.entity.Payment;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FinanceService {

    private final FinanceRepository financeRepository;

    @Transactional
    public FinanceTransaction insertRevenue(Long partnerNo, Payment payment, BigDecimal amount, String currency){
        FinanceTransaction ft = FinanceTransaction.builder()
                .partnerNo(partnerNo)
                .payment(payment)
                .type("INCOME")
                .amount(amount)
                .currency(currency)
                .createdAt(LocalDateTime.now())
                .build();
    return financeRepository.save(ft);
    }

}
