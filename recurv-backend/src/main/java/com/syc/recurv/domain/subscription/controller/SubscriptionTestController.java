package com.syc.recurv.domain.subscription.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/test-subscription")
public class SubscriptionTestController {
    private final SubscriptionSocketController subscriptionSocketController;

    @PostMapping("/broadcast")
    public ResponseEntity<String> broadcast(@RequestParam Long partnerNo,
                                            @RequestParam String billingCycle,
                                            @RequestParam String status) {
        subscriptionSocketController.broadcastSubscriptionUpdate(partnerNo, billingCycle, status);
        return ResponseEntity.ok("Sent");
    }
}