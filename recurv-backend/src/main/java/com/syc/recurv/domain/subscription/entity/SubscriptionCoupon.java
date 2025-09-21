package com.syc.recurv.domain.subscription.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "subscription_coupon")
@Getter @Setter
@IdClass(SubscriptionCouponId.class)
public class SubscriptionCoupon {

	@Id
	private Long subscriptionId;
	
	@Id
	private Long couponId;
	private LocalDateTime appliedAt;
}
