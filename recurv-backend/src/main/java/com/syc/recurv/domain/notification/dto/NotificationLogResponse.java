package com.syc.recurv.domain.notification.dto;

import com.syc.recurv.domain.notification.entity.NotificationLog;
import lombok.Builder;
import lombok.Getter;

import java.time.format.DateTimeFormatter;

@Getter
@Builder
public class NotificationLogResponse {

    private Long id;
    private Long partnerNo;
    private String type;
    private String channel;
    private String target;
    private String message;
    private String status;
    private String sentAt;

    public static NotificationLogResponse from(NotificationLog log) {
        return NotificationLogResponse.builder()
                .id(log.getNotificationId())
                .partnerNo(log.getPartnerNo())
                .type(log.getType())
                .channel(log.getChannel())
                .target(log.getTarget())
                .message(log.getMessage())
                .status(log.getStatus())
                .sentAt(log.getSentAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }
}
