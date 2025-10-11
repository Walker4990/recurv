package com.syc.recurv.domain.payment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.syc.recurv.domain.payment.dto.TossWebhookRequest;
import com.syc.recurv.domain.payment.service.PaymentWebhookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment")
public class PaymentWebhookController {

    private final PaymentWebhookService paymentWebhookService;
    private final @Lazy ObjectMapper objectMapper;

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody String rawPayload) {
        try {
            TossWebhookRequest request = objectMapper.readValue(rawPayload, TossWebhookRequest.class);
            paymentWebhookService.handleWebhook(request, rawPayload); // 비동기 처리
            return ResponseEntity.ok("Webhook accepted (async processing)");
        } catch (Exception e) {
            log.error("❌ Webhook 처리 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Webhook failed");
        }
    }

}
