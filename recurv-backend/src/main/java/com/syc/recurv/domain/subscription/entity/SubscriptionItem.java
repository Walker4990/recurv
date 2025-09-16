package com.syc.recurv.domain.subscription.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.syc.recurv.domain.billing.entity.BillingPlan;

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
@Table(name = "subscription_item")
@Getter @Setter
public class SubscriptionItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subscriptionItemId;
    private Integer quantity;
    private BigDecimal unitPrice;
}
