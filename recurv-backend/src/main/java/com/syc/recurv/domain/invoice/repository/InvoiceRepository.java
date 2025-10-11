package com.syc.recurv.domain.invoice.repository;

import com.syc.recurv.domain.invoice.entity.Invoice;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
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

    @Modifying
    @Query("UPDATE Invoice i SET i.status = 'CANCELED', i.updatedAt = CURRENT_TIMESTAMP " +
            "WHERE i.invoiceNo = :invoiceNo")
    void markAsCanceled(@Param("invoiceNo") String invoiceNo);

    @Query("SELECT i FROM Invoice i WHERE i.partnerNo = :partnerNo")
    List<Invoice> findByPartnerNo(@Param("partnerNo") Long partnerNo);
}
