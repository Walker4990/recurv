package com.syc.recurv.domain.notification.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notification_log")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    private Long partnerNo;
    private String type;        // EXPIRE / NEXT_BILLING
    private String channel;     // EMAIL / SMS
    private String target;      // 이메일 주소 or 전화번호

    @Column(columnDefinition = "TEXT")
    private String message;

    private String status;      // SUCCESS / FAIL
    private String errorMessage;
    private int retryCount;
    private LocalDateTime sentAt;

    public static NotificationLog success(Long partnerNo, String type, String channel, String target, String message) {
        return NotificationLog.builder()
                .partnerNo(partnerNo)
                .type(type)
                .channel(channel)
                .target(target)
                .message(message)
                .status("SUCCESS")
                .sentAt(LocalDateTime.now())
                .build();
    }

    public static NotificationLog fail(Long partnerNo, String type, String channel, String target, String message, String error) {
        return NotificationLog.builder()
                .partnerNo(partnerNo)
                .type(type)
                .channel(channel)
                .target(target)
                .message(message)
                .status("FAIL")
                .errorMessage(error)
                .sentAt(LocalDateTime.now())
                .build();
    }
}
