package com.syc.recurv.domain.billing.controller;

import com.syc.recurv.domain.billing.dto.InvoiceRequestDTO;
import com.syc.recurv.domain.invoice.entity.Invoice;
import com.syc.recurv.domain.invoice.repository.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/admin/billing")
public class AdminBillingController {

    private final InvoiceRepository invoiceRepository;

    @GetMapping
    public ResponseEntity<List<Invoice>> getAllInvoices() {
        return ResponseEntity.ok(invoiceRepository.findAll());
    }
    @PutMapping("/{invoiceId}")
    public ResponseEntity<Invoice> updateInvoice(@RequestBody InvoiceRequestDTO invo, @PathVariable long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("해당 청구서를 찾을 수 없습니다."));
        invoice.setStatus(invo.getStatus());
        invoiceRepository.save(invoice);
        return ResponseEntity.ok(invoice);
    }
}
