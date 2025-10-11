package com.syc.recurv.domain.payment.service;

import com.syc.recurv.domain.finance.entity.FinanceTransaction;
import com.syc.recurv.domain.finance.repository.FinanceRepository;
import com.syc.recurv.domain.invoice.entity.Invoice;
import com.syc.recurv.domain.invoice.repository.InvoiceRepository;
import com.syc.recurv.domain.payment.dto.TossWebhookRequest;
import com.syc.recurv.domain.payment.entity.Payment;
import com.syc.recurv.domain.payment.entity.PaymentMethod;
import com.syc.recurv.domain.payment.repository.PaymentMethodRepository;
import com.syc.recurv.domain.payment.repository.PaymentRepository;
import com.syc.recurv.domain.subscription.entity.Subscription;
import com.syc.recurv.domain.subscription.repository.SubscriptionRepository;
import com.syc.recurv.domain.subscription.value.BillingInfo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "payment:summary")
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final InvoiceRepository invoiceRepository;
    private final FinanceRepository financeRepository;
    private final PaymentMethodRepository paymentMethodRepository;

    @Value("${toss.webhook.secret}")
    private String secretKey;

    private final RestTemplate restTemplate = new RestTemplate();

    @Cacheable(key = "'partner:' + #partnerNo")
    public List<Payment> getRecentPayments(Long partnerNo) {
        log.info("💡 [DB Query 실행됨 - 캐시 MISS]");
        return paymentRepository.findRecentByPartnerNo(partnerNo);
    }


    @CacheEvict(allEntries = true)
    @Transactional
    public void markPaymentConfirmed(TossWebhookRequest request) {
        String orderId = request.getData().getOrderId();
        Invoice invoice = invoiceRepository.findByInvoiceNo(orderId)
                .orElseThrow(() -> new RuntimeException("인보이스 없음"));

        Long invoiceId = invoice.getInvoiceId();

        Payment payment = paymentRepository.findByInvoiceId(invoiceId)
                .orElseGet(() -> Payment.builder()
                        .invoiceId(invoiceId)
                        .partnerNo(invoice.getPartnerNo())
                        .paymentKey(request.getData().getPaymentKey())
                        .orderId(orderId)
                        .amount(request.getData().getTotalAmount())
                        .currency(request.getData().getCurrency())
                        .build()
                );

        // 이미 SUCCESS 처리된 결제는 무시
        if ("SUCCESS".equals(payment.getStatus())) {
            log.warn("이미 SUCCESS 결제, CONFIRMED 이벤트 무시: {}", orderId);
            return;
        }

        payment.setStatus("SUCCESS");
        payment.setProviderTxId(request.getData().getTransactionKey());
        payment.setApprovedAt(request.getData().getApprovedAt() != null
                ? OffsetDateTime.parse(request.getData().getApprovedAt()).toLocalDateTime()
                : null);
        paymentRepository.save(payment);

        applyPaymentSuccess(invoiceId, payment.getAmount(), payment.getCurrency(), payment.getProviderTxId());
    }
    @CacheEvict(allEntries = true)
    @Transactional
    public void markPaymentFailed(TossWebhookRequest request) {
        String orderId = request.getData().getOrderId();
        Invoice invoice = invoiceRepository.findByInvoiceNo(orderId)
                .orElseThrow(() -> new RuntimeException("인보이스 없음"));

        Long invoiceId = invoice.getInvoiceId();

        Payment payment = paymentRepository.findByInvoiceId(invoiceId)
                .orElseGet(() -> Payment.builder()
                        .invoiceId(invoiceId)
                        .partnerNo(invoice.getPartnerNo())
                        .paymentKey(request.getData().getPaymentKey())
                        .orderId(orderId)
                        .amount(request.getData().getTotalAmount())
                        .currency(request.getData().getCurrency())
                        .build()
                );

        // ✅ 이미 SUCCESS 처리된 결제는 실패로 덮어쓰지 않음
        if ("SUCCESS".equals(payment.getStatus())) {
            log.warn("이미 SUCCESS 결제, FAILED 이벤트 무시: {}", orderId);
            return;
        }

        payment.setStatus("FAILED");
        payment.setFailedReason("FAILED");
        paymentRepository.save(payment);
    }
    @CacheEvict(allEntries = true)
    @Transactional
    public void markPaymentCanceledIfActive(TossWebhookRequest request) {
        String orderId = request.getData().getOrderId();
        Invoice invoice = invoiceRepository.findByInvoiceNo(orderId)
                .orElseThrow(() -> new RuntimeException("인보이스 없음"));

        Long invoiceId = invoice.getInvoiceId();
        Long partnerNo = invoice.getPartnerNo();

        Payment payment = paymentRepository.findByInvoiceId(invoiceId)
                .orElseGet(() -> Payment.builder()
                        .invoiceId(invoiceId)
                        .partnerNo(partnerNo)
                        .paymentKey(request.getData().getPaymentKey())
                        .orderId(orderId)
                        .amount(request.getData().getTotalAmount())
                        .currency(request.getData().getCurrency())
                        .build()
                );

        // ✅ 이미 SUCCESS 처리된 결제는 취소 불가
        if ("SUCCESS".equals(payment.getStatus())) {
            log.warn("이미 SUCCESS 결제, CANCELED 이벤트 무시: {}", orderId);
            return;
        }

        payment.setStatus("CANCELED");
        payment.setFailedReason("CANCELED");
        paymentRepository.save(payment);

        subscriptionRepository.findByPartnerNo(partnerNo).ifPresent(sub -> {
            if ("ACTIVE".equals(sub.getStatus())) {
                subscriptionRepository.deactivateByPartnerNo(partnerNo);
                invoiceRepository.markAsCanceled(invoice.getInvoiceNo());
            }
        });
    }
    @CacheEvict(allEntries = true)
    @Transactional
    public void markPaymentRefunded(TossWebhookRequest request) {
        String orderId = request.getData().getOrderId();
        Long invoiceId = invoiceRepository.findByInvoiceNo(orderId)
                .orElseThrow(() -> new RuntimeException("인보이스 없음"))
                .getInvoiceId();

        Payment payment = paymentRepository.findByInvoiceId(invoiceId)
                .orElseThrow(() -> new RuntimeException("결제 없음"));

        // ✅ 이미 SUCCESS 아니면 환불 의미 없음
        if (!"SUCCESS".equals(payment.getStatus())) {
            log.warn("SUCCESS 상태가 아닌 결제, REFUND 이벤트 무시: {}", orderId);
            return;
        }

        applyRefund(invoiceId, request.getData().getTotalAmount(), request.getData().getCurrency());
    }
    @CacheEvict(allEntries = true)
    @Transactional
    public void markPaymentExpired(TossWebhookRequest request) {
        String orderId = request.getData().getOrderId();
        Invoice invoice = invoiceRepository.findByInvoiceNo(orderId)
                .orElseThrow(() -> new RuntimeException("인보이스 없음"));

        Long invoiceId = invoice.getInvoiceId();

        Payment payment = paymentRepository.findByInvoiceId(invoiceId)
                .orElseGet(() -> Payment.builder()
                        .invoiceId(invoiceId)
                        .partnerNo(invoice.getPartnerNo())
                        .paymentKey(request.getData().getPaymentKey())
                        .orderId(orderId)
                        .amount(request.getData().getTotalAmount())
                        .currency(request.getData().getCurrency())
                        .build()
                );

        // ✅ 이미 SUCCESS 처리된 결제는 만료 무시
        if ("SUCCESS".equals(payment.getStatus())) {
            log.warn("이미 SUCCESS 결제, EXPIRED 이벤트 무시: {}", orderId);
            return;
        }

        payment.setStatus("EXPIRED");
        payment.setFailedReason("EXPIRED");
        paymentRepository.save(payment);

        log.info("⌛ 결제 만료 처리 완료 - orderId={}, invoiceId={}", orderId, invoiceId);
    }

    @Transactional
    public void applyPaymentSuccess(Long invoiceId, BigDecimal amount, String currency, String transactionId) {
        Payment payment = paymentRepository.findByInvoiceId(invoiceId)
                .orElseThrow(() -> new RuntimeException("결제 없음"));

        payment.setStatus("SUCCESS");
        payment.setProviderTxId(transactionId);
        paymentRepository.save(payment);

        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("인보이스 없음"));
        Long partnerNo = invoice.getPartnerNo();

        Subscription sub = subscriptionRepository.findByPartnerNo(partnerNo)
                .orElseThrow(() -> new RuntimeException("구독 없음"));
        sub.setStatus("ACTIVE");

        if (sub.getBillingInfo() == null) {
            sub.setBillingInfo(new BillingInfo());
        }
        BillingInfo info = sub.getBillingInfo();
        info.setNextBillingDate(LocalDate.now().plusMonths(1));
        info.setBillingCycle("MONTHLY");
        info.setBillingDayOfMonth(LocalDate.now().getDayOfMonth());
        subscriptionRepository.save(sub);

        invoiceRepository.markAsPaid(invoice.getInvoiceNo());

        FinanceTransaction ft = FinanceTransaction.builder()
                .partnerNo(partnerNo)
                .payment(payment)
                .type("INCOME")
                .category("PAYMENT")
                .amount(amount)
                .currency(currency)
                .createdAt(LocalDateTime.now())
                .build();
        financeRepository.save(ft);
    }

    @Transactional
    public boolean applyRefund(Long invoiceId, BigDecimal amount, String currency) {
        Payment payment = paymentRepository.findByInvoiceId(invoiceId)
                .orElseThrow(() -> new RuntimeException("결제 없음"));

        boolean success = refundWithToss(payment.getPaymentKey(), amount);
        if (!success) throw new RuntimeException("PG사 환불 실패");

        payment.setStatus("REFUND");
        paymentRepository.save(payment);

        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("인보이스 없음"));
        Long partnerNo = invoice.getPartnerNo();
        subscriptionRepository.deactivateByPartnerNo(partnerNo);

        invoiceRepository.markAsRefund(invoice.getInvoiceNo());
        financeRepository.insertRefund(partnerNo, amount);

        return success;
    }

    public boolean refundWithToss(String paymentKey, BigDecimal amount) {
        if (paymentKey == null || paymentKey.isBlank()) {
            log.error("refundWithToss: paymentKey is null/blank");
            return false;
        }

        String url = "https://api.tosspayments.com/v1/payments/" + paymentKey + "/cancel";

        HttpHeaders headers = new HttpHeaders();
        String encodedAuth = Base64.getEncoder()
                .encodeToString((secretKey + ":").getBytes(StandardCharsets.UTF_8));
        headers.set("Authorization", "Basic " + encodedAuth);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        Map<String, Object> body = new HashMap<>();
        body.put("cancelReason", "사용자 구독 취소");
        if (amount != null) body.put("cancelAmount", amount);

        try {
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("Toss refund failed - status: {}, body: {}", e.getStatusCode(), e.getResponseBodyAsString());
            return false;
        } catch (Exception e) {
            log.error("Toss refund exception: {}", e.getMessage(), e);
            return false;
        }
    }

    @Transactional
    public void changePaymentMethod(TossWebhookRequest request) {

        String customerKey = null;
        if (request != null && request.getData() != null ) {
            customerKey = request.getData().getCustomerKey();
        }
        if (customerKey == null || customerKey.isBlank()) {
            log.warn("Webhook missing customerKey, eventId={},", request == null ? "null" : request.getEventId());
            throw new IllegalArgumentException("customerKey 누락 - partnerNo 매핑 불가");
        }

        final Long partnerNo;
        try {
            partnerNo = Long.parseLong(customerKey);
        } catch (NumberFormatException e) {
            log.error("Invalid customer format: {}, eventId={}", customerKey, request.getEventId(), e);
            throw new IllegalArgumentException("customerKey 형식 오류");
        }
        paymentMethodRepository.findByPartnerNoAndIsDefaultTrue(partnerNo)
                .ifPresent(old -> {
                    old.setIsDefault(false);
                    paymentMethodRepository.save(old);
                });

        TossWebhookRequest.Data data = request.getData();

        PaymentMethod newMethod = PaymentMethod.builder()
                .partnerNo(partnerNo)
                .type(data.getMethodType() != null ? data.getMethodType() : "CARD")
                .maskedNo(data.getMaskedNo())
                .provider(data.getProvider())
                .token(data.getBillingKey())
                .isDefault(true)
                .build();

        // validUntil이 문자열로 들어오면 파싱 (옵션)
        if (data.getValidUntil() != null && !data.getValidUntil().isBlank()) {
            try {
                newMethod.setValidUntil(LocalDate.parse(data.getValidUntil()));
            } catch (DateTimeParseException ex) {
                log.warn("validUntil parse 실패: {}, eventId={}", data.getValidUntil(), request.getEventId());
            }
        }
        PaymentMethod saved =  paymentMethodRepository.save(newMethod);

        // 5) 구독(paymentMethodId FK 방식) 갱신
        Subscription sub = subscriptionRepository.findByPartnerNo(partnerNo)
                .orElseThrow(() -> new IllegalStateException("구독 없음 partnerNo=" + partnerNo));
        sub.setPaymentMethodId(saved.getPaymentMethodId());
        subscriptionRepository.save(sub);
    }
}
