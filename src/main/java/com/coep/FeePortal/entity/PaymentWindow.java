package com.coep.FeePortal.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Entity
@Table(name = "payment_windows")
public class PaymentWindow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Year of study cannot be null")
    @Column(name = "year_of_study", nullable = false)
    private Integer yearOfStudy; // 1 (FY), 2 (SY), 3 (TY), 4 (Final Year)

    @NotBlank(message = "Academic year cannot be blank")
    @Column(name = "academic_year", nullable = false)
    private String academicYear; // e.g., "2024-2025"

    @NotNull(message = "Start date cannot be null")
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @NotNull(message = "End date cannot be null")
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    public PaymentWindow() {
    }

    public PaymentWindow(Integer yearOfStudy, String academicYear, LocalDate startDate, LocalDate endDate) {
        this.yearOfStudy = yearOfStudy;
        this.academicYear = academicYear;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isActive = true;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getYearOfStudy() { return yearOfStudy; }
    public void setYearOfStudy(Integer yearOfStudy) { this.yearOfStudy = yearOfStudy; }
    public String getAcademicYear() { return academicYear; }
    public void setAcademicYear(String academicYear) { this.academicYear = academicYear; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean active) { isActive = active; }
}
