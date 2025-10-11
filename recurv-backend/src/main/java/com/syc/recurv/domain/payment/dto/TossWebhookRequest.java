package com.syc.recurv.domain.payment.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TossWebhookRequest {
    private String eventType;   // PAYMENT_STATUS_CHANGED
    private String eventId;     // evt_xxxxx
    private Data data;          // 실제 결제 데이터

    @Getter
    @Setter
    public static class Data {
        private String orderId;
        private String paymentKey;
        private String status;         // DONE / FAILED / CANCELED / REFUNDED / EXPIRED
        private BigDecimal totalAmount;
        private String currency;
        private String approvedAt;
        private String transactionKey;

        private String billingKey;     // PG가 발급한 billingKey (token)
        private String methodType;     // CARD, ACCOUNT
        private String maskedNo;       // ****-****-1234
        private String provider;       // 카드사/은행명
        private String validUntil;     // 유효기간 (YYYY-MM-DD)
        private Long partnerNo;        // 우리 쪽 고객/파트너 식별자

        private String customerKey;

        private Failure failure;

        @Getter @Setter
        public static class Failure {
            private String code;
            private String message;
        }
    }
}
