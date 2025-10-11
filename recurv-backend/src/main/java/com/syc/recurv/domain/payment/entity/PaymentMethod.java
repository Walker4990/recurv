package com.syc.recurv.domain.payment.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "payment_method")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class PaymentMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentMethodId;

    @Column(nullable = false)
    private Long partnerNo;

    @Column(nullable = false, length = 20)
    private String type; // 'CARD','ACCOUNT' 등

    private String maskedNo;
    private String provider;
    private String token;

    @Builder.Default
    @Column(nullable = false)
    private Boolean isDefault = false; // ✅ null 방지

    private LocalDate validUntil;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

}

