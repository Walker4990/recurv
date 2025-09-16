package com.syc.recurv.domain.subscription.value;

import java.time.LocalDate;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter @Setter
public class BillingInfo {
    private String billingCycle;
    private Byte billingDayOfMonth;
    private LocalDate nextBillingDate;
}
