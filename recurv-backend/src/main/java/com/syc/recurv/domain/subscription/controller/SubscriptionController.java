package com.syc.recurv.domain.subscription.controller;

import com.syc.recurv.domain.invoice.entity.Invoice;
import com.syc.recurv.domain.invoice.repository.InvoiceRepository;
import com.syc.recurv.domain.invoice.service.InvoiceService;
import com.syc.recurv.domain.payment.entity.Payment;
import com.syc.recurv.domain.payment.repository.PaymentRepository;
import com.syc.recurv.domain.payment.service.PaymentService;
import com.syc.recurv.domain.subscription.entity.Subscription;
import com.syc.recurv.domain.subscription.repository.SubscriptionRepository;
import com.syc.recurv.domain.subscription.service.SubscriptionService;
import com.syc.recurv.domain.subscription.value.BillingInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/subscriptions")
@CrossOrigin(origins = "http://localhost:3000")
public class SubscriptionController {
    private final SubscriptionService subscriptionService;
    private final SubscriptionRepository subscriptionRepository;
    private final InvoiceRepository invoiceRepository;
    private final PaymentRepository paymentRepository;

    @PostMapping("/{id}/cancel")
    public ResponseEntity<String> cancelSubscription(@PathVariable Long id){
        try {
            subscriptionService.cancelSubscription(id);
            return ResponseEntity.ok("구독 및 환불이 정상적으로 처리되었습니다.");
        } catch (RuntimeException e) {
            // 실패 메시지 내려주기
            return ResponseEntity
                    .status(502) // Bad Gateway: PG사 실패 표현에 적합
                    .body("환불 실패: " + e.getMessage());
        }
    }
    @GetMapping("/{orderId}")
    public ResponseEntity<?> getSubscription(@PathVariable String orderId) {

        // 1) 인보이스 가져오기
        Invoice invoice = invoiceRepository.findByInvoiceNo(orderId)
                .orElseThrow(() -> new RuntimeException("인보이스 없음"));

        // 2) 구독 가져오기
        Subscription sub = subscriptionRepository.findByPartnerNo(invoice.getPartnerNo())
                .orElseThrow(() -> new RuntimeException("구독 없음"));

        // 3) 결제 가져오기 (없으면 null 처리)
        Payment payment = paymentRepository.findByInvoiceId(invoice.getInvoiceId()).orElse(null);

        // 4) 필요한 데이터 합쳐서 응답
        Map<String, Object> result = new HashMap<>();
        result.put("id", sub.getSubscriptionId());          // Subscription PK
        result.put("status", sub.getStatus());              // 구독 상태
        result.put("plan", invoice.getPlanId());            // 요금제 ID (또는 이름 매핑 필요)
        result.put("startedAt", sub.getPeriod() != null ? sub.getPeriod().getStartDate() : null);
        result.put("expiredAt", sub.getPeriod() != null ? sub.getPeriod().getEndDate() : null);

        // ✅ BillingInfo 안전 처리
        BillingInfo billingInfo = sub.getBillingInfo();
        result.put("nextBillingDate", billingInfo != null ? billingInfo.getNextBillingDate() : null);

        // ✅ Payment 존재 여부에 따라 분기
        if (payment != null) {
            result.put("amount", payment.getAmount());
            result.put("currency", payment.getCurrency());
            result.put("paymentMethod",
                    payment.getPaymentMethod().getType() + " " + payment.getPaymentMethod().getMaskedNo());
        } else {
            // 아직 결제 확정 안 된 경우
            result.put("amount", invoice.getTotalAmount());
            result.put("currency", invoice.getCurrency());
            result.put("paymentMethod", "결제 대기 중");
        }

        return ResponseEntity.ok(result);
    }

    // 내 구독정보 불러오기
    @GetMapping("/me")
    public ResponseEntity<?> getMySubscription(@RequestParam Long partnerNo) {
        // partnerNo 기준으로 구독 조회
        Subscription sub = subscriptionRepository.findByPartnerNo(partnerNo)
                .orElseThrow(() -> new RuntimeException("구독 없음"));

        Map<String, Object> result = new HashMap<>();
        result.put("id", sub.getSubscriptionId());
        result.put("status", sub.getStatus());

        // Period 값 안전하게 처리
        if (sub.getPeriod() != null) {
            result.put("startedAt", sub.getPeriod().getStartDate());
            result.put("expiredAt", sub.getPeriod().getEndDate());
        } else {
            result.put("startedAt", null);
            result.put("expiredAt", null);
        }

        // BillingInfo 값 안전하게 처리
        BillingInfo billingInfo = sub.getBillingInfo();
        result.put("nextBillingDate", billingInfo != null ? billingInfo.getNextBillingDate() : null);

        return ResponseEntity.ok(result);
    }

}
