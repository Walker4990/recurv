package com.syc.recurv.domain.payment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class PaymentSocketController {

    private final SimpMessagingTemplate messaging;

    // ✅ 단순 알림용
    public void notifyPaymentEvent(String orderId, BigDecimal amount, boolean isNewSubscription, boolean isNewPartner) {
        Map<String, Object> data = new HashMap<>();
        data.put("orderId", orderId);
        data.put("amount", amount);
        data.put("timestamp", LocalDateTime.now());
        data.put("isNewSubscription", isNewSubscription);
        data.put("isNewPartner", isNewPartner);

        messaging.convertAndSend("/topic/payments", data);
    }

    // ✅ 상태 포함 알림
    public void notifyPaymentEvent(String orderId, BigDecimal totalAmount, String status, boolean isNewSubscription, boolean isNewPartner) {
        Map<String, Object> payload = Map.of(
                "orderId", orderId,
                "amount", totalAmount,
                "status", status,
                "isNewSubscription", isNewSubscription,
                "isNewPartner", isNewPartner
        );
        messaging.convertAndSend("/topic/payments", payload);
    }
}
