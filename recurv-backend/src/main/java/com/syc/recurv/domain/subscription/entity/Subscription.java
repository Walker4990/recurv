package com.syc.recurv.domain.subscription.entity;

import java.time.LocalDateTime;

import com.syc.recurv.domain.subscription.value.BillingInfo;
import com.syc.recurv.domain.subscription.value.Period;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "subscription")
@Getter @Setter
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subscriptionId;

    private Long partnerNo;  // ERP partner FK
    private String status;

    @Embedded
    private Period period;

    @Embedded
    private BillingInfo billingInfo;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
