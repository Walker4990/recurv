package com.syc.recurv.domain.payment.service;

import com.syc.recurv.domain.finance.entity.FinanceTransaction;
import com.syc.recurv.domain.finance.repository.FinanceRepository;
import com.syc.recurv.domain.invoice.entity.Invoice;
import com.syc.recurv.domain.invoice.repository.InvoiceRepository;
import com.syc.recurv.domain.payment.entity.Payment;
import com.syc.recurv.domain.payment.repository.PaymentRepository;
import com.syc.recurv.domain.subscription.repository.SubscriptionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final InvoiceRepository invoiceRepository;
    private final FinanceRepository financeRepository;

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

    public Payment save(Payment payment) {
        return paymentRepository.save(payment);
    }
    // 클라이언트단에서 결제 요청 시 발생
    @Transactional
    public Payment processPayment(Map<String, Object> result, Long partnerNo, Long planId) {
        // 1) 결제 저장
        Payment payment = Payment.builder()
                .paymentKey((String) result.get("paymentKey"))
                .orderId((String) result.get("orderId"))
                .amount(new BigDecimal(result.get("totalAmount").toString()))
                .currency((String) result.get("currency"))
                .status((String) result.get("status"))
                .providerTxId((String) result.get("transactionKey"))
                .approvedAt(result.get("approvedAt") != null ?
                        OffsetDateTime.parse(result.get("approvedAt").toString()).toLocalDateTime() : null)
                .failedReason(result.get("failure") != null ?
                        ((Map) result.get("failure")).get("message").toString() : null)
                .partnerNo(partnerNo)
                .build();

        paymentRepository.save(payment);

        // 2) 구독 활성화
        subscriptionRepository.activateByPartnerNo(partnerNo);

        // 3) 인보이스 상태 변경
        invoiceRepository.markAsPaid((String) result.get("orderId"));

        // 4) 재무 기록 반영
        financeRepository.insertRevenue(partnerNo, payment.getAmount());

        return payment;
    }
    // 웹훅: 결제 성공
    @Transactional
    public void applyPaymentSuccess(String orderId, BigDecimal amount, String currency, String transactionId) {
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("결제 없음"));

        payment.setStatus("SUCCESS");
        payment.setProviderTxId(transactionId);
        paymentRepository.save(payment);

        Invoice invoice = invoiceRepository.findByInvoiceNo(orderId)
                .orElseThrow(() -> new RuntimeException("인보이스 없음"));

        Long partnerNo = invoice.getPartnerNo();
        Long planId = invoice.getPlanId(); // ✅ 이제 여기서 바로 가져올 수 있음

        subscriptionRepository.activateByPartnerNo(partnerNo);
        invoiceRepository.markAsPaid(orderId);

        // 4. FinanceTransaction 저장
        FinanceTransaction tx = new FinanceTransaction();
        tx.setPartnerNo(partnerNo);
        tx.setAmount(amount);
        tx.setCurrency(currency);
        tx.setPayment(payment);   // 🔥 반드시 연결
        financeRepository.save(tx);
    }
    //환불
    @Transactional
    public void applyRefund(String orderId, BigDecimal amount, String currency, String refundId){
        Payment payment = paymentRepository.findByOrderId(orderId).orElseThrow(() -> new RuntimeException("결제 없음"));
        payment.setStatus("REFUND");
        paymentRepository.save(payment);

        Invoice invoice = invoiceRepository.findByInvoiceNo(orderId)
                .orElseThrow(() -> new RuntimeException("인보이스 없음"));

        Long partnerNo = invoice.getPartnerNo();
        subscriptionRepository.deactivateByPartnerNo(partnerNo);
        invoiceRepository.markAsRefund(orderId);
        financeRepository.insertRefund(payment.getPartnerNo(), amount);
    }
    // 결제 실패 및 취소
    @Transactional
    public void applyFailure(String orderId, String status) {
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("결제 없음"));

        payment.setStatus(status); // FAILED or CANCELED
        paymentRepository.save(payment);
    }

}



