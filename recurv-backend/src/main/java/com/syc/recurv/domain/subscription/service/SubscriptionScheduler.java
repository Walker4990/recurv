package com.syc.recurv.domain.subscription.service;

import com.syc.recurv.domain.notification.service.NotificationService;
import com.syc.recurv.domain.subscription.entity.Subscription;
import com.syc.recurv.domain.subscription.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionScheduler {

    private final SubscriptionRepository subscriptionRepository;
    private final NotificationService notificationService;

    // ✅ 매일 자정 실행
    @Scheduled(cron = "0 0 0 * * *")
    public void notifyExpiringSubscriptions() {
        LocalDate targetDate = LocalDate.now().plusDays(1); // ✅ LocalDate로 변경
        List<Subscription> expiring = subscriptionRepository.findExpiringSubscriptions(targetDate);

        if (expiring.isEmpty()) {
            log.info("📭 만료 예정 구독 없음");
            return;
        }

        expiring.forEach(notificationService::sendExpiryAlert);
        log.info("📨 만료 예정 구독 {}건 알림 발송 완료", expiring.size());
    }
}
