package com.syc.recurv.domain.invoice.service;

import com.syc.recurv.domain.invoice.entity.Invoice;
import com.syc.recurv.domain.invoice.repository.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;

    public Invoice create(Invoice invoice) {
        return invoiceRepository.save(invoice);
    }

    public Invoice get(Long id) {
        return invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("인보이스 없음"));
    }

    public List<Invoice> getAll() {
        return invoiceRepository.findAll();
    }

    public Invoice update(Invoice invoice) {
        return invoiceRepository.save(invoice);
    }

    public void delete(Long id) {
        invoiceRepository.deleteById(id);
    }
}

