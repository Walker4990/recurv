package com.syc.recurv.domain.support.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/test")
public class SupportTestController {
    private final SimpMessagingTemplate smt;

    @PostMapping("/support")
    public String sendTestSupport(@RequestBody Map<String, Object> req) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("partnerNo", req.get("partnerNo"));
        payload.put("message", req.getOrDefault("message", "테스트 문의가 도착했습니다."));
        payload.put("time", java.time.LocalDateTime.now().toString());

        smt.convertAndSend("/topic/support/new", payload);
        return "✅ Support event broadcasted.";
    }
}
