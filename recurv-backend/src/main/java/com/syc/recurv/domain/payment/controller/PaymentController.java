package com.syc.recurv.domain.payment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.syc.recurv.domain.invoice.entity.Invoice;
import com.syc.recurv.domain.invoice.repository.InvoiceRepository;
import com.syc.recurv.domain.payment.service.PaymentService;
import com.syc.recurv.domain.payment.service.PaymentWebhookService;
import com.syc.recurv.domain.payment.util.SignatureValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {
    @Value("${toss.secret-key}")
    private String tossSecretKey;
    private final RestTemplate restTemplate;
    private final PaymentWebhookService paymentWebhookService;
    private final SignatureValidator signatureValidator;
    private final ObjectMapper objectMapper;
    private final InvoiceRepository invoiceRepository;
    private final PaymentService paymentService;

    /**
     *  클라이언트가 결제 직후 Toss Confirm API를 호출하는 엔드포인트
     * - Toss API 결과만 프론트에 리턴
     * - 실제 DB 반영은 Webhook에서만 처리
     */
    @PostMapping("/confirm")
    public ResponseEntity<?> confirmPayment(@RequestBody Map<String, String> body) {
        try {
            String paymentKey = body.get("paymentKey");
            String orderId = body.get("orderId");
            int amount = Integer.parseInt(body.get("amount"));

            // 1. Toss Confirm API 호출
            HttpHeaders headers = new HttpHeaders();
            headers.setBasicAuth(tossSecretKey, ""); // ✅ Secret Key는 properties에서 주입 권장
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> req = new HashMap<>();
            req.put("paymentKey", paymentKey);
            req.put("orderId", orderId);
            req.put("amount", amount);

            ResponseEntity<Map> response = restTemplate.postForEntity(
                    "https://api.tosspayments.com/v1/payments/confirm",
                    new HttpEntity<>(req, headers),
                    Map.class
            );

            Map<String, Object> result = response.getBody();
            log.info("✅ Toss Confirm API 결과 반환 - orderId={}, result={}", orderId, result);

            // 2. ✅ DB 반영은 Webhook에서 처리 (여기서는 하지 않음)
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            log.error("❌ confirmPayment 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
    @PostMapping("/mock-confirm")
    public ResponseEntity<Map<String, Object>> mockConfirm(@RequestBody Map<String, Object> body) {
        Map<String, Object> result = new HashMap<>();
        result.put("orderId", body.get("orderId"));
        result.put("status", "DONE");
        result.put("amount", body.get("amount"));
        result.put("message", "Mock confirm success");

        return ResponseEntity.ok(result);
    }
}
