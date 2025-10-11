package com.syc.recurv.domain.subscription.controller;

import com.syc.recurv.domain.subscription.entity.Subscription;
import com.syc.recurv.domain.subscription.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/subscription")
@CrossOrigin(origins = "http://localhost:3000")
public class AdminSubscriptionController {

    private final SubscriptionRepository subscriptionRepository;

    // ✅ 관리자 구독 목록 조회
    @GetMapping
    public ResponseEntity<?> getSubscriptions(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status
    ) {
        List<Subscription> subs = subscriptionRepository.findAll();

        // 검색 조건 적용
        if (search != null && !search.isEmpty()) {
            subs = subs.stream()
                    .filter(s -> String.valueOf(s.getPartnerNo()).contains(search))
                    .collect(Collectors.toList());
        }

        // 상태 필터 적용
        if (status != null && !status.isEmpty()) {
            subs = subs.stream()
                    .filter(s -> status.equalsIgnoreCase(s.getStatus()))
                    .collect(Collectors.toList());

        }
        // React가 요구하는 구조로 변환
        List<Map<String, Object>> result = subs.stream()
                .map(s -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", s.getSubscriptionId());
                    map.put("partnerNo", s.getPartnerNo());
                    map.put("status", s.getStatus());
                    map.put("billingCycle", s.getBillingInfo() != null ? s.getBillingInfo().getBillingCycle() : null);
                    map.put("billingDayOfMonth", s.getBillingInfo() != null ? s.getBillingInfo().getBillingDayOfMonth() : null);
                    map.put("nextBillingDate", s.getBillingInfo() != null ? s.getBillingInfo().getNextBillingDate() : null);
                    map.put("startDate", s.getPeriod() != null ? s.getPeriod().getStartDate() : null);
                    map.put("endDate", s.getPeriod() != null ? s.getPeriod().getEndDate() : null);
                    return map;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateSubscriptionStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> req) {

        String status = req.get("status");
        Subscription sub = subscriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("구독을 찾을 수 없습니다."));

        sub.setStatus(status);
        subscriptionRepository.save(sub);

        Map<String, Object> result = new HashMap<>();
        result.put("id", sub.getSubscriptionId());
        result.put("status", sub.getStatus());
        result.put("message", "상태 변경 완료");

        return ResponseEntity.ok(result);
    }
}
