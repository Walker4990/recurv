package com.syc.recurv.domain.subscription.value;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter @Setter
public class BillingInfo {
    @Column(name = "billing_cycle")
    private String billingCycle;

    @Column(name = "billing_day_of_month")
    private int billingDayOfMonth;

    @Column(name = "next_billing_date")
    private LocalDate nextBillingDate;
    public BillingInfo() {

    }

    public BillingInfo(String billingCycle, int billingDayOfMonth, LocalDate nextBillingDate) {
        this.billingCycle = billingCycle;
        this.billingDayOfMonth = billingDayOfMonth;
        this.nextBillingDate = nextBillingDate;
    }

}
