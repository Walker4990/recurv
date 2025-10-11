package com.syc.recurv.domain.subscription.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class SubscriptionDTO {
    private Long id;
    private String status;
    private LocalDate startDate;
    private LocalDate endDate;
    private String billingCycle;
    private Integer billingDayOfMonth;
    private LocalDate nextBillingDate;
    private Long partnerNo;
}
