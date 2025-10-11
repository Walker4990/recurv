package com.syc.recurv.domain.subscription.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class SubscriptionSocketController {
    private final SimpMessagingTemplate smt;

    public void broadcastSubscriptionUpdate(Long partnerNo, String billingCycle, String status){
        Map<String,Object> map = new HashMap<>();
        map.put("partnerNo", partnerNo);
        map.put("billingCycle", billingCycle);
        map.put("status", status);
        smt.convertAndSend("/topic/subscriptionUpdate", map);
    }

}
