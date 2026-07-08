package com.coep.FeePortal.service;

import com.coep.FeePortal.entity.FeeRecord;
import com.coep.FeePortal.entity.Transaction;
import com.coep.FeePortal.repository.FeeRecordRepository;
import com.coep.FeePortal.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PaymentService {

    @Autowired
    private FeeRecordRepository feeRecordRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ReceiptService receiptService;

    /**
     * Process payment for a fee record.
     * scholarshipAmount: amount student declares will come from scholarship (can be 0).
     * studentPayable: amountDue - scholarshipAmount — this is what the student pays now.
     */
    @Transactional
    public Transaction processPayment(Long feeRecordId, BigDecimal scholarshipAmount) {
        FeeRecord feeRecord = feeRecordRepository.findById(feeRecordId)
                .orElseThrow(() -> new RuntimeException("Fee Record not found"));

        if (feeRecord.getStatus() == FeeRecord.Status.PAID) {
            throw new RuntimeException("Fee is already fully paid");
        }
        if (feeRecord.getStatus() == FeeRecord.Status.PARTIALLY_PAID) {
            throw new RuntimeException("Student portion already paid. Scholarship is pending admin credit.");
        }

        // Validate window is still open
        if (feeRecord.getPaymentWindow() != null) {
            LocalDate today = LocalDate.now();
            if (today.isBefore(feeRecord.getPaymentWindow().getStartDate()) ||
                today.isAfter(feeRecord.getPaymentWindow().getEndDate())) {
                throw new RuntimeException("Payment window is not currently active.");
            }
        }

        if (scholarshipAmount == null) scholarshipAmount = BigDecimal.ZERO;

        BigDecimal studentPayable = feeRecord.getAmountDue().subtract(scholarshipAmount);

        // Update fee record
        feeRecord.setScholarshipAmount(scholarshipAmount);
        feeRecord.setAmountPaid(studentPayable);

        if (scholarshipAmount.compareTo(BigDecimal.ZERO) > 0) {
            feeRecord.setScholarshipStatus(FeeRecord.ScholarshipStatus.PENDING);
            feeRecord.setStatus(FeeRecord.Status.PARTIALLY_PAID); // Waiting on scholarship credit
        } else {
            feeRecord.setScholarshipStatus(FeeRecord.ScholarshipStatus.NONE);
            feeRecord.setStatus(FeeRecord.Status.PAID);
        }

        feeRecordRepository.save(feeRecord);

        // Record the transaction for the student-payable portion
        Transaction transaction = new Transaction(
                feeRecord,
                studentPayable,
                LocalDateTime.now(),
                "TXN" + UUID.randomUUID().toString().substring(0, 8).toUpperCase()
        );
        transaction = transactionRepository.save(transaction);

        // Generate receipt
        receiptService.generateReceipt(transaction);

        return transaction;
    }

    /**
     * Admin marks a scholarship amount as credited.
     * Sets scholarshipStatus = CREDITED and overall status = PAID.
     */
    @Transactional
    public FeeRecord markScholarshipCredited(Long feeRecordId) {
        FeeRecord feeRecord = feeRecordRepository.findById(feeRecordId)
                .orElseThrow(() -> new RuntimeException("Fee Record not found"));

        if (feeRecord.getScholarshipStatus() != FeeRecord.ScholarshipStatus.PENDING) {
            throw new RuntimeException("No pending scholarship to credit for this record.");
        }

        feeRecord.setScholarshipStatus(FeeRecord.ScholarshipStatus.CREDITED);
        feeRecord.setStatus(FeeRecord.Status.PAID);
        return feeRecordRepository.save(feeRecord);
    }
}
