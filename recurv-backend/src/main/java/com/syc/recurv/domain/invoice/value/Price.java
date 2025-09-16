package com.syc.recurv.domain.invoice.value;

import java.math.BigDecimal;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter @Setter
public class Price {
    private BigDecimal unitPrice;      // 단가
    private BigDecimal discountAmount; // 할인액
    private BigDecimal taxRate;        // 세율
    private BigDecimal taxAmount;      // 세액
    private BigDecimal lineAmount;     // 총액
}
