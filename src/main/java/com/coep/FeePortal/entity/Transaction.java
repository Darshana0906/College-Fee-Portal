package com.coep.FeePortal.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fee_record_id", nullable = false)
    private FeeRecord feeRecord;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "paid_at", nullable = false)
    private LocalDateTime paidAt;

    @Column(name = "reference_id", nullable = false, unique = true)
    private String referenceId; // e.g., simulated transaction ID

    public Transaction() {}

    public Transaction(FeeRecord feeRecord, BigDecimal amount, LocalDateTime paidAt, String referenceId) {
        this.feeRecord = feeRecord;
        this.amount = amount;
        this.paidAt = paidAt;
        this.referenceId = referenceId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public FeeRecord getFeeRecord() { return feeRecord; }
    public void setFeeRecord(FeeRecord feeRecord) { this.feeRecord = feeRecord; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public LocalDateTime getPaidAt() { return paidAt; }
    public void setPaidAt(LocalDateTime paidAt) { this.paidAt = paidAt; }
    public String getReferenceId() { return referenceId; }
    public void setReferenceId(String referenceId) { this.referenceId = referenceId; }
}
