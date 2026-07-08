package com.coep.FeePortal.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "receipts")
public class Receipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id", nullable = false, unique = true)
    private Transaction transaction;

    @Column(name = "receipt_number", nullable = false, unique = true)
    private String receiptNumber;

    @Column(name = "generated_at", nullable = false)
    private LocalDateTime generatedAt;

    // For simplicity, we might just re-generate it on the fly, or store the raw PDF bytes.
    // Let's just assume we re-generate or have a reference path.
    // A more advanced solution would be a blob or AWS S3 URL.
    @Lob
    @Column(name = "pdf_data", columnDefinition = "LONGBLOB")
    private byte[] pdfData;

    public Receipt() {}

    public Receipt(Transaction transaction, String receiptNumber, LocalDateTime generatedAt, byte[] pdfData) {
        this.transaction = transaction;
        this.receiptNumber = receiptNumber;
        this.generatedAt = generatedAt;
        this.pdfData = pdfData;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Transaction getTransaction() { return transaction; }
    public void setTransaction(Transaction transaction) { this.transaction = transaction; }
    public String getReceiptNumber() { return receiptNumber; }
    public void setReceiptNumber(String receiptNumber) { this.receiptNumber = receiptNumber; }
    public LocalDateTime getGeneratedAt() { return generatedAt; }
    public void setGeneratedAt(LocalDateTime generatedAt) { this.generatedAt = generatedAt; }
    public byte[] getPdfData() { return pdfData; }
    public void setPdfData(byte[] pdfData) { this.pdfData = pdfData; }
}
