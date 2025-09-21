package com.syc.recurv.domain.payment.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name="webhook_log", uniqueConstraints = @UniqueConstraint(columnNames = "eventId"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebhookLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String eventId;

    @Column(nullable = false, length = 50)
    private String status;

    @Lob
    private String payload;

    private LocalDateTime receivedAt;
    private LocalDateTime processedAt;

    @Column(length = 1000)
    private String error;
}
