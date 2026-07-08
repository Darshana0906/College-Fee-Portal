package com.coep.FeePortal.repository;

import com.coep.FeePortal.entity.PaymentWindow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentWindowRepository extends JpaRepository<PaymentWindow, Long> {
    Optional<PaymentWindow> findByYearOfStudyAndAcademicYear(Integer yearOfStudy, String academicYear);
    Optional<PaymentWindow> findByYearOfStudyAndIsActiveTrue(Integer yearOfStudy);
}
