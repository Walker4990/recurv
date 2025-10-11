package com.syc.recurv.domain.payment.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.syc.recurv.domain.invoice.entity.Invoice;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "payment")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    // FK
    private Long invoiceId;
    private Long partnerNo;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_method_id", referencedColumnName = "paymentMethodId")
    @JsonIgnore
    private PaymentMethod paymentMethod;

    // PG 관련
    @Column(name = "payment_key")
    private String paymentKey;

    @Column(name = "order_id")
    private String orderId;

    private BigDecimal amount;
    private String currency;
    private String status;

    @Column(name = "provider_tx_id")
    private String providerTxId;

    private LocalDateTime approvedAt;
    private String failedReason;

    @Column(updatable = false, insertable = false)
    private LocalDateTime createdAt;
}
