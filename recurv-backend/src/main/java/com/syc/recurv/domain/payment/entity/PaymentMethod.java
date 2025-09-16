package com.syc.recurv.domain.payment.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "payment_method")
@Getter @Setter
public class PaymentMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentMethodId;
    private Long partnerNo;
    private String type;
    private String maskedNo;
    private String provider;
    private String token;
    private Boolean isDefault;
    private LocalDate validUntil;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

