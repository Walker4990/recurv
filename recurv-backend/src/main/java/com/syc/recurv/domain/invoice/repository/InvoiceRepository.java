package com.syc.recurv.domain.invoice.repository;

import com.syc.recurv.domain.invoice.entity.Invoice;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice,Long> {
    @Modifying
    @Transactional
    @Query("UPDATE Invoice i SET i.status = 'PAID' WHERE i.invoiceNo = :invoiceNo")
    void markAsPaid(String invoiceNo);
    @Modifying
    @Transactional
    @Query("UPDATE Invoice i SET i.status = 'REFUNDED' WHERE i.invoiceNo = :invoiceNo")
    void markAsRefund(String invoiceNo);

    Optional<Invoice> findByInvoiceNo(String invoiceNo);
}
