package com.syc.recurv.domain.payment.entity;

import com.syc.recurv.domain.payment.value.DunningAction;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="dunning_log")
@Getter @Setter
public class DunningLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dunningId;

    @Column(name="invoice_id")
    private Long invoiceId;   // ID만 저장 (연관 매핑 X)

    private Integer attemptNo;

    @Embedded
    private DunningAction dunningAction;
}
