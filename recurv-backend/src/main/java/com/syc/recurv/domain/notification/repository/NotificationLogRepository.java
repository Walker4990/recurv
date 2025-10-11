package com.syc.recurv.domain.notification.repository;

import com.syc.recurv.domain.notification.entity.NotificationLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationLogRepository extends JpaRepository<NotificationLog, Long> {
}
