package com.syc.recurv.domain.subscription.entity;

import java.time.LocalDateTime;

import com.syc.recurv.domain.payment.entity.PaymentMethod;
import com.syc.recurv.domain.subscription.value.BillingInfo;
import com.syc.recurv.domain.subscription.value.Period;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "subscription")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subscriptionId;

    private Long partnerNo;  // ERP partner FK
    private String status;

    @Embedded
    private Period period;

    @Embedded
    private BillingInfo billingInfo = new BillingInfo();

    private Long paymentMethodId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void setPaymentMethod(PaymentMethod saved) {
    }
}
