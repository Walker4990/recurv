package com.syc.recurv.domain.payment.service;

import com.syc.recurv.domain.payment.dto.TossWebhookRequest;
import com.syc.recurv.domain.payment.entity.WebhookLog;
import com.syc.recurv.domain.payment.repository.WebhookLogRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentWebhookService {
    private final PaymentService paymentService;
    private final WebhookLogRepository webhookLogRepository;

    // 결제 후 토스에서 우리 서버로 이벤트를 보냈을 때 실행
    @Transactional
    public void handleWebhook(TossWebhookRequest request) {
        // Toss가 보낸 이벤트의 고유 ID
        String eventId = request.getEventId();

        // 같은 eventId가 이미 처리된 적이 있으면 중복 방지 → 바로 return
        if (eventId != null && webhookLogRepository.existsByEventId(eventId)) {
            return; // 이미 처리된 이벤트는 무시
        }

        // 새 웹훅 로그 객체 생성 (DB에 기록하기 위함)
        WebhookLog log = WebhookLog.builder()
                // eventId가 없으면 orderId:status 조합으로 대신 저장
                .eventId(eventId == null ? request.getOrderId() + ":" + request.getStatus() : eventId)
                .status(request.getStatus()) // 현재 이벤트 상태 (DONE, FAILED, CANCELED, REFUNDED)
                .payload(null) // 원본 payload 저장할 수도 있음 (여기선 null)
                .receivedAt(LocalDateTime.now()) // 이벤트 수신 시각
                .build();

        // DB에 로그 저장
        webhookLogRepository.save(log);

        try {
            // 이벤트 상태별로 분기 처리
            switch (request.getStatus()) {
                case "DONE":
                    // 결제가 정상 완료된 경우 → 결제 성공 처리
                    paymentService.applyPaymentSuccess(
                            request.getOrderId(),       // 주문 ID
                            request.getAmount(),        // 금액
                            request.getCurrency(),      // 통화 단위
                            request.getTransactionId() // 결제 트랜잭션 ID
                    );
                    break;
                case "FAILED":
                case "CANCELED":
                    // 결제 실패 또는 취소된 경우 → 실패 처리
                    paymentService.applyFailure(request.getOrderId(), request.getStatus());
                    break;
                case "REFUNDED":
                    // 결제가 환불된 경우 → 환불 처리
                    paymentService.applyRefund(
                            request.getOrderId(),
                            request.getAmount(),
                            request.getCurrency(),
                            request.getTransactionId()
                    );
                    break;
                default:
                    // 정의되지 않은 상태는 무시
            }

            // 정상적으로 처리된 경우 처리 완료 시각 기록
            log.setProcessedAt(LocalDateTime.now());
            webhookLogRepository.save(log);

        } catch (Exception e) {
            // 처리 중 오류 발생 시 로그에 에러 메시지 기록
            log.setError(e.getMessage());
            webhookLogRepository.save(log);

            // 트랜잭션 롤백을 위해 예외 다시 던짐
            throw e;
        }
    }

}
