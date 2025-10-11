package com.syc.recurv.domain.notification.service;

import com.syc.recurv.domain.notification.entity.NotificationLog;
import com.syc.recurv.domain.notification.repository.NotificationLogRepository;
import com.syc.recurv.domain.subscription.entity.Subscription;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final EmailService emailService;
    private final PartnerInfoService partnerInfoService;
    private final NotificationLogRepository logRepository;

    @Async
    public void sendExpiryAlert(Subscription sub) {
        PartnerContact contact = partnerInfoService.findContactByPartnerNo(sub.getPartnerNo());
        if (contact == null || contact.getEmail() == null) {
            log.warn("📭 이메일 없음: partnerNo={}", sub.getPartnerNo());
            return;
        }

        String message = String.format(
                "[Recurv] '%s' 구독이 %s에 만료됩니다. 서비스 이용을 원하시면 재구독 해주세요.",
                sub.getBillingInfo().getBillingCycle(),
                sub.getPeriod().getEndDate()
        );

        try {
            emailService.send(contact.getEmail(), "Recurv 구독 만료 안내", message);

            logRepository.save(NotificationLog.builder()
                    .partnerNo(sub.getPartnerNo())
                    .type("EXPIRE")
                    .channel("EMAIL")
                    .target(contact.getEmail())
                    .message(message)
                    .status("SUCCESS")
                    .sentAt(LocalDateTime.now())
                    .build());

            log.info("✅ 이메일 알림 발송 완료: partnerNo={}, email={}", sub.getPartnerNo(), contact.getEmail());
        } catch (Exception e) {
            logRepository.save(NotificationLog.builder()
                    .partnerNo(sub.getPartnerNo())
                    .type("EXPIRE")
                    .channel("EMAIL")
                    .target(contact.getEmail())
                    .message(message)
                    .status("FAIL")
                    .errorMessage(e.getMessage())
                    .sentAt(LocalDateTime.now())
                    .build());

            log.error("❌ 이메일 발송 실패: {}", e.getMessage());
        }
    }
}
