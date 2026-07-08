package com.coep.FeePortal.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "fee_structures", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"category_id", "academic_year", "year_of_study"})
})
public class FeeStructure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(name = "academic_year", nullable = false)
    private String academicYear; // e.g., "2025-2026"

    @Column(name = "year_of_study", nullable = false)
    private Integer yearOfStudy; // 1=FY, 2=SY, 3=TY, 4=Final Year

    // ===== Fee Components =====

    @Column(name = "tuition_fee", nullable = false, precision = 10, scale = 2)
    private BigDecimal tuitionFee = BigDecimal.ZERO;

    @Column(name = "development_fee", nullable = false, precision = 10, scale = 2)
    private BigDecimal developmentFee = BigDecimal.ZERO;

    @Column(name = "other_fee", nullable = false, precision = 10, scale = 2)
    private BigDecimal otherFee = BigDecimal.ZERO;

    @Column(name = "exam_fee", nullable = false, precision = 10, scale = 2)
    private BigDecimal examFee = BigDecimal.ZERO;

    // Alumni Association Fee (Rs. 750) + Disaster Relief Fund Fee (Rs. 10) = Rs. 760
    @Column(name = "miscellaneous_fee", nullable = false, precision = 10, scale = 2)
    private BigDecimal miscellaneousFee = BigDecimal.ZERO;

    /** Computed total — sum of all components. Not stored in DB. */
    public BigDecimal getTotal() {
        return tuitionFee
                .add(developmentFee)
                .add(otherFee)
                .add(examFee)
                .add(miscellaneousFee);
    }

    public FeeStructure() {}

    public FeeStructure(Category category, String academicYear, Integer yearOfStudy,
                        BigDecimal tuitionFee, BigDecimal developmentFee,
                        BigDecimal otherFee, BigDecimal examFee, BigDecimal miscellaneousFee) {
        this.category = category;
        this.academicYear = academicYear;
        this.yearOfStudy = yearOfStudy;
        this.tuitionFee = tuitionFee;
        this.developmentFee = developmentFee;
        this.otherFee = otherFee;
        this.examFee = examFee;
        this.miscellaneousFee = miscellaneousFee;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
    public String getAcademicYear() { return academicYear; }
    public void setAcademicYear(String academicYear) { this.academicYear = academicYear; }
    public Integer getYearOfStudy() { return yearOfStudy; }
    public void setYearOfStudy(Integer yearOfStudy) { this.yearOfStudy = yearOfStudy; }
    public BigDecimal getTuitionFee() { return tuitionFee; }
    public void setTuitionFee(BigDecimal tuitionFee) { this.tuitionFee = tuitionFee; }
    public BigDecimal getDevelopmentFee() { return developmentFee; }
    public void setDevelopmentFee(BigDecimal developmentFee) { this.developmentFee = developmentFee; }
    public BigDecimal getOtherFee() { return otherFee; }
    public void setOtherFee(BigDecimal otherFee) { this.otherFee = otherFee; }
    public BigDecimal getExamFee() { return examFee; }
    public void setExamFee(BigDecimal examFee) { this.examFee = examFee; }
    public BigDecimal getMiscellaneousFee() { return miscellaneousFee; }
    public void setMiscellaneousFee(BigDecimal miscellaneousFee) { this.miscellaneousFee = miscellaneousFee; }
}
