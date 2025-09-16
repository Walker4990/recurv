package com.syc.recurv.domain.coupon.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="coupon")
@Getter @Setter
public class Coupon {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long couponId;
	
    private String code;
    private String type;
    private BigDecimal value;
    private BigDecimal maxDiscount;
    private LocalDate validFrom;
    private LocalDate validUntil;
    private Integer usageLimit;
    private Integer usedCount;
    private Boolean isActive;

    private LocalDateTime createdAt;
}
