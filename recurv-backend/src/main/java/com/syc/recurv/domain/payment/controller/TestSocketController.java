package com.syc.recurv.domain.payment.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/test-socket")
public class TestSocketController {
    private final SimpMessagingTemplate smt;

    @PostMapping("/payment")
    public String testPaymentEvent(@RequestParam String orderId, @RequestParam int amount) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("orderId", orderId);
        payload.put("amount", amount);
        payload.put("timestamp", LocalDateTime.now().toString());
        smt.convertAndSend("/topic/payments", payload);

        return "Sent test payment event to /topic/payments";
    }
}
