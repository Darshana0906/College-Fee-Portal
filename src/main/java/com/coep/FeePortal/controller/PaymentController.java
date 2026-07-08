package com.coep.FeePortal.controller;

import com.coep.FeePortal.entity.FeeRecord;
import com.coep.FeePortal.entity.Receipt;
import com.coep.FeePortal.entity.Transaction;
import com.coep.FeePortal.repository.ReceiptRepository;
import com.coep.FeePortal.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private ReceiptRepository receiptRepository;

    /**
     * Simulate student payment.
     * Body: { "scholarshipAmount": 10000 }  (optional; defaults to 0 if absent)
     */
    @PostMapping("/{feeRecordId}")
    public ResponseEntity<Transaction> simulatePayment(
            @PathVariable Long feeRecordId,
            @RequestBody(required = false) PaymentRequest request) {

        BigDecimal scholarship = (request != null && request.getScholarshipAmount() != null)
                ? request.getScholarshipAmount()
                : BigDecimal.ZERO;

        Transaction transaction = paymentService.processPayment(feeRecordId, scholarship);
        return ResponseEntity.ok(transaction);
    }

    @GetMapping("/receipts/{transactionId}/download")
    public ResponseEntity<byte[]> downloadReceipt(@PathVariable Long transactionId) {
        Receipt receipt = receiptRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new RuntimeException("Receipt not found"));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", receipt.getReceiptNumber() + ".pdf");

        return new ResponseEntity<>(receipt.getPdfData(), headers, HttpStatus.OK);
    }

    static class PaymentRequest {
        private BigDecimal scholarshipAmount;
        public BigDecimal getScholarshipAmount() { return scholarshipAmount; }
        public void setScholarshipAmount(BigDecimal scholarshipAmount) { this.scholarshipAmount = scholarshipAmount; }
    }
}
