package com.syc.recurv.domain.payment.controller;

import com.syc.recurv.domain.payment.dto.PaymentMethodDTO;
import com.syc.recurv.domain.payment.entity.PaymentMethod;
import com.syc.recurv.domain.payment.service.PaymentMethodService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/payment-methods")
@RequiredArgsConstructor
public class PaymentMethodController {
    private final PaymentMethodService paymentMethodService;

    @PostMapping("/change")
    public ResponseEntity<PaymentMethod> changePaymentMethod(
            @RequestParam Long partnerNo,
            @RequestBody @Valid PaymentMethodDTO dto) {

        PaymentMethod newMethod = dto.toEntity(partnerNo);
        PaymentMethod saved = paymentMethodService.changePaymentMethod(partnerNo, newMethod);
        return ResponseEntity.ok(saved);
    }
}
