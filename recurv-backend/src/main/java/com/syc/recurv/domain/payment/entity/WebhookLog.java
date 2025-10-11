package com.syc.recurv.domain.payment.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name="webhook_log", uniqueConstraints = @UniqueConstraint(columnNames = "event_id"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebhookLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_id", nullable = false, length = 255, unique = true)
    private String eventId;

    @Column(nullable = false, length = 50)
    private String status;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String payload;

    @Column(name = "received_at")
    private LocalDateTime receivedAt;

    @Column(name = "processed_at")
    private LocalDateTime processedAt;

    @Column(length = 1000)
    private String error;
}

