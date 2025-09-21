package com.syc.recurv.domain.payment.repository;

import com.syc.recurv.domain.payment.entity.WebhookLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WebhookLogRepository  extends JpaRepository<WebhookLog,Long> {
    boolean existsByEventId(String eventId);
}
