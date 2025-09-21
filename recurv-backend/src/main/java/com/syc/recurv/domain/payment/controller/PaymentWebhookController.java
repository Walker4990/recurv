package com.syc.recurv.domain.payment.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.syc.recurv.domain.payment.dto.TossWebhookRequest;
import com.syc.recurv.domain.payment.service.PaymentWebhookService;
import com.syc.recurv.domain.payment.util.SignatureValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PaymentWebhookController {
    private final PaymentWebhookService paymentWebhookService;
    private final ObjectMapper objectMapper;
    private final SignatureValidator signatureValidator;

    @PostMapping("/webhook/toss")
    public ResponseEntity<String> onTossWebhook(@RequestBody String payload,
                                                @RequestHeader("Toss-Signature") String signature) throws JsonProcessingException {
//        if (!signatureValidator.verify(payload, signature)){
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid signature");
//    }
        if (!signatureValidator.verify(payload, signature)) {
            log.error("Signature verification failed"); // 이제 log 사용 가능
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
            TossWebhookRequest request = objectMapper.readValue(payload, TossWebhookRequest.class);
            paymentWebhookService.handleWebhook(request);
            return ResponseEntity.ok("ok");
        }
    }
