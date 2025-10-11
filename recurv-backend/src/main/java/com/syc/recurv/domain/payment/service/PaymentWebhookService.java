package com.syc.recurv.domain.payment.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.syc.recurv.domain.invoice.repository.InvoiceRepository;
import com.syc.recurv.domain.payment.controller.PaymentSocketController;
import com.syc.recurv.domain.payment.dto.TossWebhookRequest;
import com.syc.recurv.domain.payment.entity.WebhookLog;
import com.syc.recurv.domain.payment.repository.WebhookLogRepository;
import com.syc.recurv.domain.subscription.repository.SubscriptionRepository;
import com.syc.recurv.domain.users.repository.UsersRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentWebhookService {

    private final PaymentService paymentService;
    private final WebhookLogRepository webhookLogRepository;
    private final InvoiceRepository invoiceRepository;
    private final ObjectMapper objectMapper;
    private final PaymentSocketController socketController;
    private final SubscriptionRepository subscriptionRepository;
    private final UsersRepository usersRepository;

    @Async("taskExecutor")
    @Transactional
    public void handleWebhook(TossWebhookRequest request, String rawPayload) {
        if (request == null) {
            log.error("❌ Webhook 요청 자체가 null입니다");
            return;
        }
        if (request.getData() == null) {
            log.error("❌ Webhook data가 null입니다: {}", request);
            return;
        }
        if (request.getData().getOrderId() == null) {
            log.error("❌ Webhook orderId가 null입니다. data: {}", request.getData());
            return;
        }
        String eventId = request.getEventId();
        String orderId = request.getData() != null ? request.getData().getOrderId() : null;
        String status = request.getData() != null ? request.getData().getStatus() : "UNKNOWN";
        BigDecimal totalAmount = request.getData() != null ? request.getData().getTotalAmount() : BigDecimal.ZERO;
        Long partnerNo = request.getData() != null ? request.getData().getPartnerNo() : null;
        log.info(">>> Webhook 진입 - orderId={}, status={}, eventId={}", orderId, status, eventId);

        if (eventId != null && webhookLogRepository.existsByEventId(eventId)) {
            log.warn("중복 Webhook 이벤트 무시: {}", eventId);
            return;
        }

        WebhookLog logEntity = WebhookLog.builder()
                .eventId(eventId != null ? eventId : (orderId + ":" + status))
                .status(status)
                .payload(rawPayload)
                .receivedAt(LocalDateTime.now())
                .status("RECEIVED")
                .build();
        webhookLogRepository.save(logEntity);

        try {

            switch (status) {
                case "DONE", "APPROVED", "CONFIRMED", "SUCCESS" -> {
                    log.info("✅ 결제 성공 이벤트 수신 - orderId={}, status={}", orderId, status);
                    paymentService.markPaymentConfirmed(request);

                    // 신규 구독 / 신규 거래처 여부 판별 (추정)
                    boolean isNewSubscription = (partnerNo != null && !subscriptionRepository.existsByPartnerNo(partnerNo));
                    boolean isNewPartner = (partnerNo != null && !usersRepository.existsById(partnerNo));


                    // ✅ 성공 WebSocket 알림 전송
                    socketController.notifyPaymentEvent(orderId, totalAmount, status, isNewSubscription, isNewPartner);
                }
                case "FAILED" -> {
                    log.info("❌ 결제 실패 이벤트 수신 - orderId={}", orderId);
                    paymentService.markPaymentFailed(request);
                    socketController.notifyPaymentEvent(orderId, totalAmount, "FAILED", false, false);
                }
                case "CANCELED" -> {
                    log.info("⚠️ 결제 취소 이벤트 수신 - orderId={}", orderId);
                    paymentService.markPaymentCanceledIfActive(request);
                    socketController.notifyPaymentEvent(orderId, totalAmount, "CANCELED", false, false);
                }
                case "REFUNDED" -> {
                    log.info("💸 환불 이벤트 수신 - orderId={}", orderId);
                    paymentService.markPaymentRefunded(request);
                    socketController.notifyPaymentEvent(orderId, totalAmount, "REFUNDED", false, false);
                }
                case "EXPIRED" -> {
                    log.info("⌛ 결제 만료 이벤트 수신 - orderId={}", orderId);
                    paymentService.markPaymentExpired(request);
                }
                case "BILLING_KEY_ISSUED", "BILLING_KEY_UPDATED" -> {
                    log.info("💳 결제수단 변경 이벤트 수신 - customerKey={}", request.getData().getCustomerKey());
                    paymentService.changePaymentMethod(request);
                }
                default -> log.warn("❓ 처리하지 않는 상태 수신: {}", status);
            }

            logEntity.setProcessedAt(LocalDateTime.now());
            webhookLogRepository.save(logEntity);

        } catch (Exception e) {
            log.error("Webhook 처리 실패 - orderId={}, status={}, error={}",
                    orderId, status, e.getMessage(), e);
            logEntity.setError(e.getMessage());
            webhookLogRepository.save(logEntity);
            throw e;
        }
    }
}
