package com.syc.recurv.domain.invoice.service;

import com.syc.recurv.domain.invoice.entity.InvoiceItem;
import com.syc.recurv.domain.invoice.repository.InvoiceItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InvoiceItemService {
    private final InvoiceItemRepository invoiceItemRepository;

    public InvoiceItem create(InvoiceItem item) {
        return invoiceItemRepository.save(item);
    }

    public InvoiceItem get(Long id) {
        return invoiceItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("인보이스 아이템 없음"));
    }

    public List<InvoiceItem> getAll() {
        return invoiceItemRepository.findAll();
    }

    public InvoiceItem update(InvoiceItem item) {
        return invoiceItemRepository.save(item);
    }

    public void delete(Long id) {
        invoiceItemRepository.deleteById(id);
    }
}

