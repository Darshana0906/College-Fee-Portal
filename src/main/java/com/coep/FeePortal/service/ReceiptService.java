package com.coep.FeePortal.service;

import com.coep.FeePortal.entity.Receipt;
import com.coep.FeePortal.entity.Transaction;
import com.coep.FeePortal.repository.ReceiptRepository;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ReceiptService {

    @Autowired
    private ReceiptRepository receiptRepository;

    public Receipt generateReceipt(Transaction transaction) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, out);
            document.open();

            document.add(new Paragraph("College Fee Portal - Receipt"));
            document.add(new Paragraph("--------------------------------------------------"));
            document.add(new Paragraph("Transaction ID: " + transaction.getReferenceId()));
            document.add(new Paragraph("Date: " + transaction.getPaidAt().toString()));
            document.add(new Paragraph("Student: " + transaction.getFeeRecord().getStudent().getName()));
            document.add(new Paragraph("Course: " + transaction.getFeeRecord().getStudent().getCourse()));
            document.add(new Paragraph("Year of Study: " + transaction.getFeeRecord().getYearOfStudy()));
            document.add(new Paragraph("Amount Paid: Rs. " + transaction.getAmount()));
            document.add(new Paragraph("--------------------------------------------------"));
            document.add(new Paragraph("Thank you for your payment."));

            document.close();

            byte[] pdfBytes = out.toByteArray();

            Receipt receipt = new Receipt(
                    transaction,
                    "REC-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase(),
                    LocalDateTime.now(),
                    pdfBytes
            );

            return receiptRepository.save(receipt);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate receipt PDF", e);
        }
    }
}
