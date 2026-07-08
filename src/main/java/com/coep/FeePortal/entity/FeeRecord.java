package com.coep.FeePortal.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "fee_records", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"student_id", "year_of_study"})
})
public class FeeRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(name = "academic_year", nullable = false)
    private String academicYear;

    @Column(name = "year_of_study", nullable = false)
    private Integer yearOfStudy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_window_id", nullable = true)
    private PaymentWindow paymentWindow;

    @Column(name = "sequence_number", nullable = false)
    private Integer sequenceNumber;

    // Snapshot of total fee due (from FeeStructure.getTotal() at time of unlock)
    @Column(name = "amount_due", nullable = false, precision = 10, scale = 2)
    private BigDecimal amountDue;

    // Amount the student actually paid (= amountDue - scholarshipAmount)
    @Column(name = "amount_paid", nullable = false, precision = 10, scale = 2)
    private BigDecimal amountPaid = BigDecimal.ZERO;

    // Scholarship amount declared by student (to be paid directly to college)
    @Column(name = "scholarship_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal scholarshipAmount = BigDecimal.ZERO;

    // NONE = no scholarship; PENDING = declared but not yet credited; CREDITED = admin confirmed receipt
    public enum ScholarshipStatus { NONE, PENDING, CREDITED }

    @Enumerated(EnumType.STRING)
    @Column(name = "scholarship_status", nullable = false)
    private ScholarshipStatus scholarshipStatus = ScholarshipStatus.NONE;

    // PENDING = window open, not yet paid
    // PARTIALLY_PAID = student paid their share, scholarship still pending
    // PAID = fully settled
    // OVERDUE = window closed before payment
    public enum Status { PENDING, PARTIALLY_PAID, PAID, OVERDUE }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.PENDING;

    // Link back to the FeeStructure snapshot for breakdown display
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fee_structure_id", nullable = true)
    private FeeStructure feeStructure;

    public FeeRecord() {}

    public FeeRecord(Student student, String academicYear, Integer yearOfStudy,
                     Integer sequenceNumber, BigDecimal amountDue,
                     PaymentWindow paymentWindow, FeeStructure feeStructure) {
        this.student = student;
        this.academicYear = academicYear;
        this.yearOfStudy = yearOfStudy;
        this.sequenceNumber = sequenceNumber;
        this.amountDue = amountDue;
        this.paymentWindow = paymentWindow;
        this.feeStructure = feeStructure;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
    public String getAcademicYear() { return academicYear; }
    public void setAcademicYear(String academicYear) { this.academicYear = academicYear; }
    public Integer getYearOfStudy() { return yearOfStudy; }
    public void setYearOfStudy(Integer yearOfStudy) { this.yearOfStudy = yearOfStudy; }
    public PaymentWindow getPaymentWindow() { return paymentWindow; }
    public void setPaymentWindow(PaymentWindow paymentWindow) { this.paymentWindow = paymentWindow; }
    public Integer getSequenceNumber() { return sequenceNumber; }
    public void setSequenceNumber(Integer sequenceNumber) { this.sequenceNumber = sequenceNumber; }
    public BigDecimal getAmountDue() { return amountDue; }
    public void setAmountDue(BigDecimal amountDue) { this.amountDue = amountDue; }
    public BigDecimal getAmountPaid() { return amountPaid; }
    public void setAmountPaid(BigDecimal amountPaid) { this.amountPaid = amountPaid; }
    public BigDecimal getScholarshipAmount() { return scholarshipAmount; }
    public void setScholarshipAmount(BigDecimal scholarshipAmount) { this.scholarshipAmount = scholarshipAmount; }
    public ScholarshipStatus getScholarshipStatus() { return scholarshipStatus; }
    public void setScholarshipStatus(ScholarshipStatus scholarshipStatus) { this.scholarshipStatus = scholarshipStatus; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    public FeeStructure getFeeStructure() { return feeStructure; }
    public void setFeeStructure(FeeStructure feeStructure) { this.feeStructure = feeStructure; }
}
