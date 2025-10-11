package com.syc.recurv.domain.invoice.controlloer;

import com.syc.recurv.domain.invoice.entity.Invoice;
import com.syc.recurv.domain.invoice.service.InvoiceService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/invoices")
@CrossOrigin(origins = "http://localhost:3000") // 프론트 로컬 접근 허용
public class InvoiceController {

    private final InvoiceService invoiceService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody CreateInvoiceReq req) {
        Invoice inv = invoiceService.createInvoice(req.getPartnerNo(), req.getPlanId(), req.getAmount());
        // 프론트에서 Toss에 바로 넣어 쓸 수 있게 핵심 필드만 반환
        return ResponseEntity.ok(Map.of(
                "invoiceNo", inv.getInvoiceNo(),      // == orderId로 사용
                "totalAmount", inv.getTotalAmount(),
                "currency", inv.getCurrency()
        ));
    }

    @Data
    public static class CreateInvoiceReq {
        private Long partnerNo;
        private Long planId;
        private BigDecimal amount;
    }
}
