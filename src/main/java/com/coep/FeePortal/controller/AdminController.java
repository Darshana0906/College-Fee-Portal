package com.coep.FeePortal.controller;

import com.coep.FeePortal.entity.Category;
import com.coep.FeePortal.entity.FeeRecord;
import com.coep.FeePortal.entity.FeeStructure;
import com.coep.FeePortal.entity.PaymentWindow;
import com.coep.FeePortal.repository.CategoryRepository;
import com.coep.FeePortal.repository.FeeRecordRepository;
import com.coep.FeePortal.repository.FeeStructureRepository;
import com.coep.FeePortal.service.PaymentService;
import com.coep.FeePortal.service.WindowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired private CategoryRepository categoryRepository;
    @Autowired private FeeStructureRepository feeStructureRepository;
    @Autowired private FeeRecordRepository feeRecordRepository;
    @Autowired private WindowService windowService;
    @Autowired private PaymentService paymentService;

    @PostMapping("/categories")
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        return ResponseEntity.ok(categoryRepository.save(category));
    }

    @PostMapping("/fee-structure")
    public ResponseEntity<FeeStructure> createFeeStructure(@RequestBody FeeStructureRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        FeeStructure fs = new FeeStructure(
                category,
                request.getAcademicYear(),
                request.getYearOfStudy(),
                request.getTuitionFee(),
                request.getDevelopmentFee(),
                request.getOtherFee(),
                request.getExamFee(),
                BigDecimal.ZERO // miscellaneousFee defaults to 0 for admin manual entry unless added to request
        );
        return ResponseEntity.ok(feeStructureRepository.save(fs));
    }

    @PostMapping("/payment-windows")
    public ResponseEntity<PaymentWindow> openPaymentWindow(@RequestBody WindowRequest request) {
        PaymentWindow window = windowService.openPaymentWindow(
                request.getYearOfStudy(),
                request.getAcademicYear(),
                LocalDate.parse(request.getStartDate()),
                LocalDate.parse(request.getEndDate())
        );
        return ResponseEntity.ok(window);
    }

    /** Get all fee records where scholarship is pending — for admin scholarship management */
    @GetMapping("/scholarship-pending")
    public ResponseEntity<List<FeeRecord>> getScholarshipPendingRecords() {
        List<FeeRecord> records = feeRecordRepository.findAll()
                .stream()
                .filter(r -> r.getScholarshipStatus() == FeeRecord.ScholarshipStatus.PENDING)
                .collect(Collectors.toList());
        return ResponseEntity.ok(records);
    }

    /** Admin confirms scholarship amount has been credited to the college account */
    @PutMapping("/fee-records/{feeRecordId}/scholarship-credit")
    public ResponseEntity<FeeRecord> markScholarshipCredited(@PathVariable Long feeRecordId) {
        return ResponseEntity.ok(paymentService.markScholarshipCredited(feeRecordId));
    }

    // DTOs
    static class FeeStructureRequest {
        private Long categoryId;
        private String academicYear;
        private Integer yearOfStudy;
        private BigDecimal tuitionFee;
        private BigDecimal examFee;
        private BigDecimal developmentFee;
        private BigDecimal otherFee;

        public Long getCategoryId() { return categoryId; }
        public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
        public String getAcademicYear() { return academicYear; }
        public void setAcademicYear(String academicYear) { this.academicYear = academicYear; }
        public Integer getYearOfStudy() { return yearOfStudy; }
        public void setYearOfStudy(Integer yearOfStudy) { this.yearOfStudy = yearOfStudy; }
        public BigDecimal getTuitionFee() { return tuitionFee; }
        public void setTuitionFee(BigDecimal tuitionFee) { this.tuitionFee = tuitionFee; }
        public BigDecimal getExamFee() { return examFee; }
        public void setExamFee(BigDecimal examFee) { this.examFee = examFee; }
        public BigDecimal getDevelopmentFee() { return developmentFee; }
        public void setDevelopmentFee(BigDecimal developmentFee) { this.developmentFee = developmentFee; }
        public BigDecimal getOtherFee() { return otherFee; }
        public void setOtherFee(BigDecimal otherFee) { this.otherFee = otherFee; }
    }

    static class WindowRequest {
        private Integer yearOfStudy;
        private String academicYear;
        private String startDate;
        private String endDate;

        public Integer getYearOfStudy() { return yearOfStudy; }
        public void setYearOfStudy(Integer yearOfStudy) { this.yearOfStudy = yearOfStudy; }
        public String getAcademicYear() { return academicYear; }
        public void setAcademicYear(String academicYear) { this.academicYear = academicYear; }
        public String getStartDate() { return startDate; }
        public void setStartDate(String startDate) { this.startDate = startDate; }
        public String getEndDate() { return endDate; }
        public void setEndDate(String endDate) { this.endDate = endDate; }
    }
}
