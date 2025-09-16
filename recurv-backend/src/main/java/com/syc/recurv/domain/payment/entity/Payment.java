package com.syc.recurv.domain.payment.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.syc.recurv.domain.invoice.entity.Invoice;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "payment")
@Getter @Setter
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;
    private Long partnerNo;
    private BigDecimal amount;
    private String currency;
    private String status;
    private String providerTxId;
    private LocalDateTime approvedAt;
    private String failedReason;
    private LocalDateTime createdAt;
}
