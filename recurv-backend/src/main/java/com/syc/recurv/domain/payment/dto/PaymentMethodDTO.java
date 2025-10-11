package com.syc.recurv.domain.payment.dto;

import com.syc.recurv.domain.payment.entity.PaymentMethod;
import lombok.*;

import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentMethodDTO {
    private String type;       // 카드 or 계좌
    private String maskedNo;   // ****-****-1234
    private String provider;   // 카드사 / 은행명
    private String token;      // Toss에서 받은 billingKey
    private LocalDate validUntil;

    // ✅ 엔티티 변환 메서드
    public PaymentMethod toEntity(Long partnerNo) {
        return PaymentMethod.builder()
                .partnerNo(partnerNo)
                .type(type)
                .maskedNo(maskedNo)
                .provider(provider)
                .token(token)
                .validUntil(validUntil)
                .isDefault(true) // 새 결제수단은 기본값
                .build();
    }
}
