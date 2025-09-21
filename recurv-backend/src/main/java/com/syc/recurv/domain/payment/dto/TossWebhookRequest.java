package com.syc.recurv.domain.payment.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TossWebhookRequest {
    private String eventType;      // PAYMENT_STATUS_CHANGED
    private String eventId;        // evt_1234567890
    private String status;         // DONE / FAILED / CANCELED / REFUNDED
    private String orderId;        // order_12345
    private String paymentKey;     // pay_abcdefghijk
    private String transactionId;  // txn_987654321
    private BigDecimal amount;     // 결제 금액
    private String currency;       // KRW
    private String approvedAt;     // 2025-09-21T14:30:00+09:00
}
