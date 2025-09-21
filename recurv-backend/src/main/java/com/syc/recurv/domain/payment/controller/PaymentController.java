package com.syc.recurv.domain.payment.controller;

import com.syc.recurv.domain.payment.dto.TossWebhookRequest;
import com.syc.recurv.domain.payment.entity.Payment;
import com.syc.recurv.domain.payment.service.PaymentService;
import com.syc.recurv.domain.payment.service.PaymentWebhookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final RestTemplate restTemplate = new RestTemplate();
    private final PaymentService paymentService;
    private final PaymentWebhookService paymentwebhookService;
    @PostMapping("/confirm")
    public ResponseEntity<?> confirmPayment(@RequestBody Map<String, String> body) {
        String paymentKey = body.get("paymentKey");
        String orderId = body.get("orderId");
        String amount = body.get("amount");

        // Toss API 호출
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("test_sk_ma60RZblrqoaLvBo6j2R3wzYWBn1", ""); // Secret Key
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> req = new HashMap<>();
        req.put("paymentKey", paymentKey);
        req.put("orderId", orderId);
        req.put("amount", amount);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(req, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                "https://api.tosspayments.com/v1/payments/confirm",
                entity,
                Map.class
        );

        Map<String, Object> result = response.getBody();

        paymentService.processPayment(result, 123L, 456L);

        return ResponseEntity.ok(response.getBody());
    }
    @PostMapping("/webhook/toss")
    public ResponseEntity<String> onTossWebhook(@RequestBody TossWebhookRequest request) {
        paymentwebhookService.handleWebhook(request);
        return ResponseEntity.ok("ok");
    }

}
