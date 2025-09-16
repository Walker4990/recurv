package com.syc.recurv.domain.invoice.entity;

import com.syc.recurv.domain.invoice.value.Price;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "invoice_item")
@Getter @Setter
public class InvoiceItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long invoiceItemId;

    private String description;
    private Integer quantity;

    @Embedded
    private Price price;
}
