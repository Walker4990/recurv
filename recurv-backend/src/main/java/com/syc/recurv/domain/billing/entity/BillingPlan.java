package com.syc.recurv.domain.billing.entity;

import java.math.BigDecimal;
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
@Table(name = "billing_plan")
@Getter @Setter
public class BillingPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long planId;

    private String planCode;
    private String name;
    private BigDecimal price;
    private String billingCycle;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

